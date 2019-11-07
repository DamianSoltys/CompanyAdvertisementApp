package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.NaturalPersonRepository;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.persistence.repository.VerificationTokenRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.servicelayer.dto.mapper.NaturalPersonDtoMapper;
import local.project.Inzynierka.servicelayer.dto.user.AccountRelatedDeletedEntities;
import local.project.Inzynierka.servicelayer.dto.user.AuthenticatedUserPersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.user.BecomeNaturalPersonDto;
import local.project.Inzynierka.servicelayer.dto.user.PersonRelatedDeletedEntities;
import local.project.Inzynierka.servicelayer.dto.user.UpdatePersonalDataDto;
import local.project.Inzynierka.servicelayer.dto.user.UpdateUserDto;
import local.project.Inzynierka.servicelayer.dto.user.UserInfoDto;
import local.project.Inzynierka.servicelayer.errors.IllegalPasswordException;
import local.project.Inzynierka.servicelayer.errors.InvalidVoivodeshipException;
import local.project.Inzynierka.servicelayer.errors.PasswordsNotMatchingException;
import local.project.Inzynierka.servicelayer.validation.PasswordCreatorService;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserFacade {

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final VoivodeshipRepository voivodeshipRepository;

    private final NaturalPersonRepository naturalPersonRepository;

    private final AddressRepository addressRepository;

    private final UserPersistenceService userPersistenceService;

    private final PasswordCreatorService passwordCreatorService;

    private final NaturalPersonDtoMapper naturalPersonDtoMapper;

    public UserFacade(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, VoivodeshipRepository voivodeshipRepository, NaturalPersonRepository naturalPersonRepository, AddressRepository addressRepository, UserPersistenceService userPersistenceService, PasswordCreatorService passwordCreatorService, NaturalPersonDtoMapper naturalPersonDtoMapper) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.voivodeshipRepository = voivodeshipRepository;
        this.naturalPersonRepository = naturalPersonRepository;
        this.addressRepository = addressRepository;
        this.userPersistenceService = userPersistenceService;
        this.passwordCreatorService = passwordCreatorService;
        this.naturalPersonDtoMapper = naturalPersonDtoMapper;
    }

    public User findByName(String name) {

        return userRepository.findByName(name);
    }

    public User findByEmailAddress(String emailAddress) {

        return userRepository.getByAddressEmail(emailAddress);
    }

    public User createNewUser(User user) {

        user.setAccountType((short) 0);
        return userRepository.save(user);
    }

    @Transactional
    public void createVerificationTokenForUser(String userEmail, final String token) {
        VerificationToken myToken = new VerificationToken(token);

        User user = this.userRepository.getByAddressEmail(userEmail);
        VerificationToken createdToken = verificationTokenRepository.save(myToken);
        user.setVerificationToken(createdToken);
        userRepository.save(user);
    }

    @Transactional
    public boolean verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return false;
        }

        User user = userRepository.findByVerificationToken(verificationToken);
        user.setEnabled(true);

        userRepository.save(user);
        return true;
    }

    @Transactional
    public Optional<AuthenticatedUserPersonalDataDto> becomeNaturalPerson(BecomeNaturalPersonDto naturalPersonDto,
                                                                          UserAccount userAccount) {

        NaturalPerson naturalPerson = this.naturalPersonDtoMapper.
                map(naturalPersonDto);

        naturalPerson.setAddress(
                this.addressRepository.save(this.buildAddress(naturalPerson)));

        naturalPerson = this.naturalPersonRepository.save(naturalPerson);
        User user = this.userRepository.getByAddressEmail(userAccount.getEmail());

        user.setNaturalPerson(naturalPerson);
        this.userRepository.save(user);

        return Optional.of(this.naturalPersonDtoMapper.map(naturalPerson));
    }

    private Address buildAddress(NaturalPerson naturalPerson) {

        Voivoideship voivoideship = this.voivodeshipRepository.findByName(naturalPerson.getAddress().getVoivodeship_id().getName()).orElseThrow(InvalidVoivodeshipException::new);
        return Address.builder()
                .apartmentNo(naturalPerson.getAddress().getApartmentNo())
                .buildingNo(naturalPerson.getAddress().getBuildingNo())
                .city(naturalPerson.getAddress().getCity())
                .voivodeship_id(voivoideship)
                .street(naturalPerson.getAddress().getStreet())
                .build();
    }

    public Optional<AuthenticatedUserPersonalDataDto> getUsersPersonalData(UserAccount userAccount) {

        NaturalPerson naturalPerson = this.naturalPersonRepository
                .findById(userAccount.personId())
                .orElseThrow(IllegalStateException::new);
        return Optional.of(this.naturalPersonDtoMapper.map(naturalPerson));
    }

    public Optional<UserInfoDto> getUser(Long id) {

        return Optional.of(this.userPersistenceService.getUserInfo(id));
    }

    public void changePassword(UpdateUserDto updateUserDto, UserAccount userAccount) {

        User user = this.userRepository.findByName(userAccount.getLoginName());
        this.validatePasswordChange(updateUserDto, user);

        user.setPasswordHash(this.passwordCreatorService.encodePassword(updateUserDto.getNewPassword()));
        this.userRepository.save(user);
    }

    private void validatePasswordChange(UpdateUserDto updateUserDto, User user) {
        if (!this.passwordCreatorService.comparePasswordHashes(
                updateUserDto.getCurrentPassword(), user.getPasswordHash())) {
            throw new PasswordsNotMatchingException("Provided password does not match your old password");
        }

        if (!this.passwordCreatorService.isPasswordValid(updateUserDto.getNewPassword())) {
            throw new IllegalPasswordException("Provided password has not passed the validation");
        }
    }

    public Optional<AuthenticatedUserPersonalDataDto> updatePersonalData(UpdatePersonalDataDto updatePersonalDataDto, UserAccount userAccount) {

        NaturalPerson person = this.naturalPersonRepository.findById(userAccount.personId()).orElseThrow(IllegalStateException::new);
        NaturalPerson updatedPerson = this.naturalPersonRepository.save(this.getUpdatedPerson(person, updatePersonalDataDto));

        return Optional.of(this.naturalPersonDtoMapper.map(updatedPerson));

    }

    private NaturalPerson getUpdatedPerson(NaturalPerson person, UpdatePersonalDataDto updatePersonalDataDto) {
        if (updatePersonalDataDto.getFirstName() != null) {
            if (!person.getFirstName().equals(updatePersonalDataDto.getFirstName())) {
                person.setFirstName(updatePersonalDataDto.getFirstName());
            }
        }
        if (updatePersonalDataDto.getLastName() != null) {
            if (!person.getLastName().equals(updatePersonalDataDto.getLastName())) {
                person.setLastName(updatePersonalDataDto.getLastName());
            }
        }
        if (updatePersonalDataDto.getPhoneNo() != null) {
            if (!person.getPhoneNo().equals(updatePersonalDataDto.getPhoneNo())) {
                person.setPhoneNo(updatePersonalDataDto.getPhoneNo());
            }
        }
        local.project.Inzynierka.servicelayer.dto.address.Address addressDto = updatePersonalDataDto.getAddress();
        if (addressDto != null) {
            Address address = person.getAddress();
            if (addressDto.getApartmentNo() != null) {
                if (!addressDto.getApartmentNo().equals(address.getApartmentNo())) {
                    address.setApartmentNo(addressDto.getApartmentNo());
                }
            }
            if (addressDto.getBuildingNo() != null) {
                if (!addressDto.getBuildingNo().equals(address.getBuildingNo())) {
                    address.setBuildingNo(addressDto.getApartmentNo());
                }
            }
            if (addressDto.getCity() != null) {
                if (!addressDto.getCity().equals(address.getCity())) {
                    address.setCity(addressDto.getCity());
                }
            }
            if (addressDto.getStreet() != null) {
                if (!addressDto.getStreet().equals(address.getStreet())) {
                    address.setStreet(addressDto.getStreet());
                }
            }
            if (addressDto.getVoivodeship() != null) {
                if (!addressDto.getVoivodeship().toString().equals(address.getVoivodeship_id().getName())) {
                    Voivoideship voivoideship = this.voivodeshipRepository.findByName(addressDto.getVoivodeship().toString()).orElseThrow(InvalidVoivodeshipException::new);
                    address.setVoivodeship_id(voivoideship);
                }
            }
            if (!address.equals(person.getAddress())) {
                person.setAddress(address);
            }
        }

        return person;
    }

    @Transactional
    public Optional<PersonRelatedDeletedEntities> deletePersonalData(UserAccount userAccount) {

        User user = this.userRepository.findByName(userAccount.getLoginName());
        NaturalPerson naturalPerson = user.getNaturalPerson();
        PersonRelatedDeletedEntities personRelatedDeletedEntities = this.getPersonRelatedDeletedEntities(naturalPerson);

        //TODO Put it in priority queue that run asynchronously
        //TODO Send an email with personalData/companies removal confirmation
        this.naturalPersonRepository.deleteById(naturalPerson.getId());

        return Optional.of(personRelatedDeletedEntities);
    }

    private PersonRelatedDeletedEntities getPersonRelatedDeletedEntities(NaturalPerson naturalPerson) {
        return PersonRelatedDeletedEntities.builder()
                .personId(naturalPerson.getId())
                .addressId(naturalPerson.getAddress().getId())
                .companiesIds(this.naturalPersonRepository.getAllRegisteredCompaniesIds(naturalPerson.getId()))
                .build();
    }

    @Transactional
    public Optional<AccountRelatedDeletedEntities> deleteAccount(UserAccount userAccount) {

        User user = this.userRepository.findByName(userAccount.getLoginName());
        AccountRelatedDeletedEntities accountRelatedDeletedEntities = AccountRelatedDeletedEntities.builder()
                .userId(user.getId())
                .commentsIds(this.userPersistenceService.getCommentsOfUser(user.getId()))
                .ratingIds(this.userPersistenceService.getRatingsOfUser(user.getId()))
                .subscriptionIds(this.userPersistenceService.getSubscriptionsOfUser(user.getId()))
                .emailId(user.getEmailAddressEntity().getId())
                .verificationTokenId(user.getVerificationToken() ==
                                             null ? null : user.getVerificationToken().getId()) // TODO Remove unused verification tokens in batch job instead
                .personRelatedDeletedEntities(user.getNaturalPerson() == null ?
                                                      null : this.getPersonRelatedDeletedEntities(user.getNaturalPerson()))
                .build();

        //TODO Put it in priority queue that run asynchronously
        //TODO Send an email with account removal confirmation
        this.userRepository.delete(user);

        return Optional.of(accountRelatedDeletedEntities);
    }
}

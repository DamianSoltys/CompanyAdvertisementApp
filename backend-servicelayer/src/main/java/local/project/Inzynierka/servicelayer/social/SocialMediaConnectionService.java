package local.project.Inzynierka.servicelayer.social;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.FacebookToken;
import local.project.Inzynierka.persistence.entity.SocialProfile;
import local.project.Inzynierka.persistence.repository.FacebookTokenRepository;
import local.project.Inzynierka.persistence.repository.SocialProfileRepository;
import local.project.Inzynierka.servicelayer.dto.social.ConnectionStatus;
import local.project.Inzynierka.servicelayer.dto.social.SocialPlatform;
import local.project.Inzynierka.servicelayer.dto.social.SocialProfileConnectionDto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialMediaConnectionService {

    private final SocialProfileRepository socialProfileRepository;
    private final FacebookTokenRepository facebookTokenRepository;

    public SocialMediaConnectionService(SocialProfileRepository socialProfileRepository, FacebookTokenRepository facebookTokenRepository) {
        this.socialProfileRepository = socialProfileRepository;
        this.facebookTokenRepository = facebookTokenRepository;
    }

    public List<SocialProfileConnectionDto> getSocialProfileConnections(Company company) {
        return socialProfileRepository.findByCompany(company).stream()
                .map(profile -> SocialProfileConnectionDto.builder()
                        .socialPlatform(SocialPlatform.fromSocialPlatform(profile.getSocialMediaPlatform()
                                                                                  .getSocialMediaPlatform()))
                        .connectionStatus(getConnectionStatus(profile))
                        .build())
                .collect(Collectors.toList());
    }

    private ConnectionStatus getConnectionStatus(SocialProfile profile) {
        ConnectionStatus connectionStatus;
        if (profile.getSocialMediaPlatform().getSocialMediaPlatform().equalsIgnoreCase(SocialPlatform.TWITTER.toString())) {
            connectionStatus = ConnectionStatus.NOT_CONNECTED;
        } else if (profile.getSocialMediaPlatform().getSocialMediaPlatform().equalsIgnoreCase(SocialPlatform.FACEBOOK.toString())) {
            List<FacebookToken> facebookTokens = facebookTokenRepository.findByFacebookSocialProfile_SocialProfile(profile);
            if (facebookTokens.isEmpty()) {
                connectionStatus = ConnectionStatus.NOT_CONNECTED;
            } else {
                connectionStatus = facebookTokens.stream()
                        .anyMatch(this::existValidToken) ? ConnectionStatus.CONNECTED : ConnectionStatus.EXPIRED_CONNECTION;
            }
        } else {
            throw new UnsupportedSocialMediaPlatformException();
        }

        return connectionStatus;
    }

    private boolean existValidToken(FacebookToken token) {
        return Timestamp.from(Instant.now()).before(Timestamp.from(Instant.ofEpochSecond(token.getExpiresAt())));
    }
}

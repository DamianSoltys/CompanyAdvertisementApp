package local.project.Inzynierka.servicelayer.social;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.FacebookToken;
import local.project.Inzynierka.persistence.entity.SocialProfile;
import local.project.Inzynierka.persistence.entity.TokenScopeType;
import local.project.Inzynierka.persistence.repository.FacebookTokenRepository;
import local.project.Inzynierka.persistence.repository.FacebookTokenScopesRepository;
import local.project.Inzynierka.persistence.repository.SocialProfileRepository;
import local.project.Inzynierka.servicelayer.dto.social.ConnectionStatus;
import local.project.Inzynierka.servicelayer.dto.social.SocialPlatform;
import local.project.Inzynierka.servicelayer.dto.social.SocialProfileConnectionDto;
import local.project.Inzynierka.servicelayer.dto.social.Status;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialMediaConnectionService {

    private final SocialProfileRepository socialProfileRepository;
    private final FacebookTokenRepository facebookTokenRepository;
    private final FacebookTokenScopesRepository facebookTokenScopesRepository;

    public SocialMediaConnectionService(SocialProfileRepository socialProfileRepository, FacebookTokenRepository facebookTokenRepository, FacebookTokenScopesRepository facebookTokenScopesRepository) {
        this.socialProfileRepository = socialProfileRepository;
        this.facebookTokenRepository = facebookTokenRepository;
        this.facebookTokenScopesRepository = facebookTokenScopesRepository;
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
            connectionStatus = ConnectionStatus.builder()
                    .status(Status.NOT_CONNECTED)
                    .message("Twitter is not supported for now.")
                    .build();
        } else if (profile.getSocialMediaPlatform().getSocialMediaPlatform().equalsIgnoreCase(SocialPlatform.FACEBOOK.toString())) {
            connectionStatus = handleFacebookConnectionStatus(profile);
        } else {
            throw new UnsupportedSocialMediaPlatformException();
        }

        return connectionStatus;
    }

    private ConnectionStatus handleFacebookConnectionStatus(SocialProfile profile) {
        ConnectionStatus connectionStatus;
        List<FacebookToken> facebookTokens = facebookTokenRepository.findByFacebookSocialProfile_SocialProfile(profile);
        if (facebookTokens.isEmpty()) {
            connectionStatus = ConnectionStatus.builder()
                    .status(Status.NOT_CONNECTED)
                    .message("There is no attempted connections to facebook from this account.")
                    .build();
        } else {
            List<FacebookToken> tokens = facebookTokens.stream()
                    .filter(this::existValidByTimeFacebookToken
                    ).collect(Collectors.toList());
            if (tokens.isEmpty()) {
                connectionStatus = ConnectionStatus.builder().status(Status.EXPIRED_CONNECTION).build();
            } else {
                tokens = getTokensWithRequiredPermissions(tokens);
                if (tokens.isEmpty()) {
                    connectionStatus = ConnectionStatus.builder()
                            .message(String.format("To use application, user has to grant following scopes: %s", getRequiredScopes()))
                            .status(Status.LACK_OF_REQUIRED_FACEBOOK_PERMISSIONS).build();
                } else {
                    connectionStatus = hasPageConnected(tokens) ?
                            ConnectionStatus.builder()
                                    .status(Status.CONNECTED)
                                    .build() :
                            ConnectionStatus.builder()
                                    .status(Status.LACK_OF_PAGE)
                                    .message("To use application, user has to have connected page.")
                                    .build();
                }

            }

        }
        return connectionStatus;
    }

    private boolean hasPageConnected(List<FacebookToken> tokens) {
        return tokens.stream().anyMatch(token ->
                                                token.getFacebookSocialProfile().getPageId() !=
                                                        null);
    }

    private List<FacebookToken> getTokensWithRequiredPermissions(List<FacebookToken> tokens) {
        return tokens.stream().filter(token ->
                                              facebookTokenScopesRepository.
                                                      findByFacebookTokenAndTokenScopeType(token, TokenScopeType.NORMAL).containsAll(getRequiredScopes()))
                .collect(Collectors.toList());
    }


    private List<String> getRequiredScopes() {
        return Arrays.asList("manage_pages", "publish_pages", "public_profile");
    }

    private boolean existValidByTimeFacebookToken(FacebookToken token) {
        return Timestamp.from(Instant.now()).before(Timestamp.from(Instant.ofEpochSecond(token.getExpiresAt()))) ||
                token.getExpiresAt() == 0;
    }
}

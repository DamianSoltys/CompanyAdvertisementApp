package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FacebookToken;
import local.project.Inzynierka.persistence.entity.FacebookTokenScope;
import local.project.Inzynierka.persistence.entity.TokenScopeType;

import java.util.List;

public interface FacebookTokenScopesRepository  extends ApplicationBigRepository<FacebookTokenScope> {

    List<FacebookTokenScope> findByFacebookTokenAndTokenScopeType(FacebookToken facebookToken, TokenScopeType tokenScopeType);
}

package dev.posadskiy.skillrepeat.controller.security;

import com.posadskiy.restsecurity.controller.UserSecurityController;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.exception.UserDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class UserSecurityControllerImpl implements UserSecurityController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public boolean isUserExist(String userId) {
		return userRepository.findById(userId).isPresent();
	}

	@Override
	public List<String> getUserRoles(String userId) {
		final Optional<DbUser> foundUser = userRepository.findById(userId);
		if (!foundUser.isPresent()) throw new UserDoesNotExistException();
		
		return foundUser.get().getRoles();
	}
}

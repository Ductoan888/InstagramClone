package com.insta.instagram.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.User;
import com.insta.instagram.repository.UserRepository;
import com.insta.instagram.security.JwtTokenClaims;
import com.insta.instagram.security.JwtTokenProvider;
@Service
public class UserServiceImplementation implements UserService{

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Override
	public User registerUser(User user) throws UserException {
		// TODO Auto-generated method stub
		Optional<User> isEmailExist = userRepository.findByEmail(user.getEmail());
		
		if (isEmailExist.isPresent()) {
			throw new UserException("Email is Already Exist");
		}
		Optional<User> isUsernameExist = userRepository.findByUsername(user.getUsername());
		
		if (isUsernameExist.isPresent()) {
			throw new UserException("Username is Already Exist");
		}
		
		if (user.getEmail()==null || user.getPassword()==null || user.getUsername()==null || user.getName()==null) {
			throw new UserException("all filds are required");
			
		}

		User newUser = new User();
		
		newUser.setEmail(user.getEmail());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setUsername(user.getUsername());
		newUser.setName(user.getName());
		
		
		return userRepository.save(newUser);
	}

	@Override
	public User findUserById(Integer userId) throws UserException {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			return user.get();
			
		}
		throw new UserException("User not found with id " +userId);
		
	}

	@Override
	public User findUserProfile(String token) throws UserException {
		// TODO Auto-generated method stub
		
		token = token.substring(7);
		
		JwtTokenClaims tokenClaims = jwtTokenProvider.getClaimsFromToken(token);
		String email = tokenClaims.getUsername();
		
		Optional<User> opt = userRepository.findByEmail(email);
		
		if (opt.isPresent()) {
			return opt.get();
			
		}
		
		throw new UserException("invalid token...");
	}

	@Override
	public User findUserByUsername(String username) throws UserException {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UserException("User not found with username: " + username);
	}

	@Override
	public String followUser(Integer reqUserId, Integer followUserId) throws UserException {
		// TODO Auto-generated method stub
		User reqUser = findUserById(reqUserId);
		User followUser = findUserById(followUserId);
		
		UserDto follower  = new UserDto();
		follower.setEmail(reqUser.getEmail());
		follower.setId(reqUser.getId());
		follower.setName(reqUser.getName());
		follower.setUsername(reqUser.getUsername());
		follower.setUserImage(reqUser.getImage());
		
		UserDto following = new UserDto();
		following.setEmail(follower.getEmail());
		following.setId(follower.getId());
		following.setUserImage(follower.getUserImage());
		following.setUsername(follower.getUsername());
		following.setName(follower.getName());
		
		reqUser.getFollowing().add(following);
		followUser.getFollower().add(follower);
		userRepository.save(followUser);
		userRepository.save(reqUser);
		return "You are following" + followUser.getUsername();
	}

	@Override
	public String unFollowUser(Integer reqUserId, Integer followUserId) throws UserException {
		// TODO Auto-generated method stub
		User reqUser = findUserById(reqUserId);
		User followUser = findUserById(followUserId);
		
		UserDto follower  = new UserDto();
		follower.setEmail(reqUser.getEmail());
		follower.setId(reqUser.getId());
		follower.setName(reqUser.getName());
		follower.setUsername(reqUser.getUsername());
		follower.setUserImage(reqUser.getImage());
		
		UserDto following = new UserDto();
		following.setEmail(follower.getEmail());
		following.setId(follower.getId());
		following.setUserImage(follower.getUserImage());
		following.setUsername(follower.getUsername());
		following.setName(follower.getName());
		
		reqUser.getFollowing().remove(following);
		followUser.getFollower().remove(follower);
		userRepository.save(followUser);
		userRepository.save(reqUser);
		return "you have Unfollowed" + follower.getUsername();
	}

	@Override
	public List<User> findUserByIds(List<Integer> userIds) throws UserException {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findAllUsersByUserIds(userIds);
		
		return users;
	}

	@Override
	public List<User> searchUser(String query) throws UserException {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findByQuery(query);
		if (users.size()==0) {
			throw new UserException("user not found" + query);
			
		}
		return users;
	}

	@Override
	public User updateUserDetails(User updateUser, User existingUser) throws UserException {
		// TODO Auto-generated method stub
		
		if (updateUser.getEmail() != null) {
			existingUser.setEmail(updateUser.getEmail());
			
		}
		if (updateUser.getBio()!= null) {
			existingUser.setBio(updateUser.getBio());
			
		}
		if (updateUser.getName()!=null) {
			existingUser.setName(updateUser.getName());
			
		}
		if (updateUser.getUsername()!=null) {
			existingUser.setUsername(updateUser.getUsername());
			
		}
		if (updateUser.getMobile()!=null) {
			existingUser.setMobile(updateUser.getMobile());
			
		}
		if (updateUser.getGender()!=null) {
			existingUser.setGender(updateUser.getGender());
			
		}
		if (updateUser.getWebsite()!=null) {
			existingUser.setWebsite(updateUser.getWebsite());
			
			
		}
		if (updateUser.getImage()!=null) {
			existingUser.setImage(updateUser.getImage());
			
		}
		if (updateUser.getId().equals(existingUser.getId())) {
			return userRepository.save(existingUser);
			
		}
		throw new UserException("You can't update User");
	}

}

package com.insta.instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.StoryException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Story;
import com.insta.instagram.modal.User;
import com.insta.instagram.repository.StoryRepository;
import com.insta.instagram.repository.UserRepository;
@Service
public class StoryServiceImplemention implements StoryService {

	@Autowired 
	StoryRepository storyRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRepository userRepository;
	@Override
	public Story createStory(Story story, Integer userId) throws UserException {
		// TODO Auto-generated method stub
		User user = userService.findUserById(userId);
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		userDto.setUserImage(user.getImage());
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getName());
		story.setUser(userDto);
		story.setTimestamp(LocalDateTime.now());
		user.getStories().add(story);
		
		return storyRepository.save(story);
	}

	@Override
	public List<Story> findStoryByUserId(Integer userId) throws UserException, StoryException {
		// TODO Auto-generated method stub
		User user = userService.findUserById(userId);
		List<Story> stories = user.getStories();
		if (stories.size()==0) {
			throw  new StoryException("Not Found Story");
			
		}
		return stories;
	}

	
	
	
}

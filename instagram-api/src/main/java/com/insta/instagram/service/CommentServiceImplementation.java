package com.insta.instagram.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Comment;
import com.insta.instagram.modal.Post;
import com.insta.instagram.modal.User;
import com.insta.instagram.repository.CommentRepository;
import com.insta.instagram.repository.PostRepository;
@Service
public class CommentServiceImplementation implements CommentService {

	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserService userService;
	@Autowired
	PostService postService;
	@Autowired
	PostRepository postRepository;
	@Override
	public Comment createComment(Comment comment, Integer postId, Integer userId) throws UserException, PostException {
		User user = userService.findUserById(userId);
		Post post = postService.findPostById(postId);
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setUserImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setId(user.getId());
		comment.setUser(userDto);
		comment.setCreatedAt(LocalDateTime.now());
		
		Comment createdComment = commentRepository.save(comment);
		
		post.getComments().add(createdComment);
		
		postRepository.save(post);
		return createdComment;
	}

	@Override
	public Comment findCommentById(Integer commentId) throws CommentException {
		// TODO Auto-generated method stub
		Optional<Comment> comment = commentRepository.findById(commentId);
		if (comment.isPresent()) {
			return comment.get();
			
		}
		throw new CommentException("Comment is not Exist with id:" + commentId);
	}

	@Override
	public Comment likeComment(Integer CommentId, Integer userId) throws CommentException, UserException {
		// TODO Auto-generated method stub
		User user = userService.findUserById(userId);
		Comment comment = findCommentById(CommentId);
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setUserImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setId(user.getId());
		comment.getLikedByUsers().add(userDto);
		return commentRepository.save(comment);
	}

	@Override
	public Comment unlikeComment(Integer commentId, Integer userId) throws CommentException, UserException {
		User user = userService.findUserById(userId);
		Comment comment = findCommentById(commentId);
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setUsername(user.getUsername());
		userDto.setUserImage(user.getImage());
		userDto.setName(user.getName());
		userDto.setId(user.getId());
		comment.getLikedByUsers().remove(userDto);
		return commentRepository.save(comment);
	}
	

}

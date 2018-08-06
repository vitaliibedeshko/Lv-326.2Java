package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.exception.FileOperationException;
import com.softserve.edu.cajillo.exception.PasswordMismatchException;
import com.softserve.edu.cajillo.exception.RequestEntityToLargeException;
import com.softserve.edu.cajillo.exception.UserNotFoundException;
import com.softserve.edu.cajillo.repository.UserRepository;
import com.softserve.edu.cajillo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find user with id=";
    private static final String USER_PASSWORD_MISMATCH_MESSAGE = "Password mismatch";
    private static final String UNSUPPORTED_MIME_TYPES_ERROR_MESSAGE = "Unsupported media type";
    private static final String FILES_SAVE_ERROR_MESSAGE = "Could not save file for user with id=%s";
    private static final String REQUEST_ENTITY_TOO_LARGE_ERROR_MESSAGE = "Request Entity Too Large";
    private static final String USER_USERNAME_NOT_FOUND_MESSAGE_BY_EMAIL = "Could not find user with email=";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public void updateUser(Long userId, UpdateUserDto userDto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + userId));
        String oldPassword = userDto.getOldPassword();
        String newPassword = userDto.getNewPassword();
        String repeatPassword = userDto.getRepeatPassword();
        if (oldPassword != null) {
            if ((newPassword != null) && newPassword.equals(repeatPassword)
                    && passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                throw new PasswordMismatchException(USER_PASSWORD_MISMATCH_MESSAGE);
            }
        }
        currentUser.setFirstName(userDto.getFirstName());
        currentUser.setLastName(userDto.getLastName());
        userRepository.save(currentUser);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException(USER_USERNAME_NOT_FOUND_MESSAGE_BY_EMAIL + email));
    }

    public void uploadAvatar(Long userId, MultipartFile avatar) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE));
        List<String> mimeTypes = Arrays.asList("image/jpeg", "image/pjpeg", "image/png");
        if (avatar.getSize() > (256 * 1024)) {
            throw new RequestEntityToLargeException(REQUEST_ENTITY_TOO_LARGE_ERROR_MESSAGE);
        }
        System.out.println(avatar.getSize());
        if (!mimeTypes.contains(avatar.getContentType())) {
            throw new UnsupportedOperationException(UNSUPPORTED_MIME_TYPES_ERROR_MESSAGE);
        }
        try {
            user.setAvatar(Base64.getMimeEncoder().encodeToString(avatar.getBytes()));
            userRepository.save(user);
        } catch (IOException e) {
            log.error(e.toString());
            throw new FileOperationException(String.format(FILES_SAVE_ERROR_MESSAGE, userId));
        }
    }

    @Override
    public AvatarDto getUserAvatar(Long userId) {
        return new AvatarDto(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE)).getAvatar());
    }
}
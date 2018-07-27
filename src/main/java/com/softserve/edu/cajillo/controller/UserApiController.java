package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.UserConverter;
import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userConverter.convertToDto(userService.getUser(id));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateUser(@RequestBody UpdateUserDto userDto) {
        userService.updateUser(userConverter.convertToEntity(userDto), userDto.getOldPassword(),
                userDto.getNewPassword(), userDto.getRepeatPassword());
    }
}
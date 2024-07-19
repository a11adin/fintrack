package ru.renatrenat.fintrack.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.renatrenat.fintrack.dto.UserCreateDto;
import ru.renatrenat.fintrack.mapper.UserMapper;
import ru.renatrenat.fintrack.dto.UserAdminDto;
import ru.renatrenat.fintrack.model.User;
import ru.renatrenat.fintrack.repository.UserRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Page<UserAdminDto> getList(@ParameterObject Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserAdminDto> userAdminDtoPage = users.map(userMapper::toDto);
        return userAdminDtoPage;
    }

    @GetMapping("/{id}")
    public UserAdminDto getOne(@PathVariable("id") Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        UserAdminDto userAdminDto = userMapper.toDto(userOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
        return userAdminDto;
    }

    @GetMapping("/by-ids")
    public List<UserAdminDto> getMany(@RequestParam("ids") List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        List<UserAdminDto> userAdminDtos = users.stream()
                .map(userMapper::toDto)
                .toList();
        return userAdminDtos;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UserCreateDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setRoles(dto.getRoles());
        userRepository.save(user);
        return ResponseEntity.ok("User created");
    }

    @PatchMapping("/{id}")
    public UserAdminDto patch(@PathVariable("id") long id, @RequestBody JsonNode patchNode) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        UserAdminDto userAdminDto = userMapper.toDto(user);
        objectMapper.readerForUpdating(userAdminDto).readValue(patchNode);
        userMapper.updateWithNull(userAdminDto, user);

        User resultUser = userRepository.save(user);
        return userMapper.toDto(resultUser);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam("ids") List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<User> users = userRepository.findAllById(ids);

        for (User user : users) {
            UserAdminDto userAdminDto = userMapper.toDto(user);
            objectMapper.readerForUpdating(userAdminDto).readValue(patchNode);
            userMapper.updateWithNull(userAdminDto, user);
        }

        List<User> resultUsers = userRepository.saveAll(users);
        List<Long> ids1 = resultUsers.stream()
                .map(User::getId)
                .toList();
        return ids1;
    }

    @DeleteMapping("/{id}")
    public UserAdminDto delete(@PathVariable("id") long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return userMapper.toDto(user);
    }


    //TODO сделать чтобы работал
    @DeleteMapping
    public void deleteMany(@RequestParam("id") List<Long> ids) {
        userRepository.deleteAllById(ids);
    }
}

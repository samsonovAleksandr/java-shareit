package ru.practicum.gateway.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.user.dto.User;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserClient userClient;


    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable int id) {
        return userClient.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid User user) {
        return userClient.create(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        return userClient.delete(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        return userClient.getAll();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> putUser(@RequestBody Map<Object, Object> fields,
                                          @PathVariable int id) {
        return userClient.update(id, fields);
    }

}

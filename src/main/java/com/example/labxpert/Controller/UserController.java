package com.example.labxpert.Controller;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.labxpert.Dtos.MaterialDto;
import com.example.labxpert.Dtos.PatientDto;
import com.example.labxpert.Dtos.UserDto;
import com.example.labxpert.Exception.MessageErrorException.MessageError;
import com.example.labxpert.Service.IUserService;
import com.example.labxpert.config.helper.JWTHeleper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.labxpert.config.Constant.JWTUtil.AUTH_HEADER;
import static com.example.labxpert.config.Constant.JWTUtil.SECRET;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final IUserService iUserService;

    private JWTHeleper jwtHelper;



    @GetMapping
    @PreAuthorize("hasAuthority('Responsable')")
    public ResponseEntity<List<UserDto>> getAll()
    {
        return ResponseEntity.ok(iUserService.getAll());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Responsable')")
    public ResponseEntity<UserDto> save(@RequestBody @Valid UserDto userDto)
    {
        UserDto userSaved = iUserService.add(userDto);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('Responsable')")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody @Valid UserDto userDto)
    {
        UserDto userUpdated = iUserService.update(id, userDto);
        return new ResponseEntity<>(userUpdated,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Responsable')")
    public ResponseEntity<UserDto> getById(@PathVariable Long id)
    {
        try{
            UserDto userDto = iUserService.getById(id);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }catch (NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('Responsable')")
    public ResponseEntity<MessageError> delete(@PathVariable Long id)
    {
        MessageError messageError = new MessageError("Material deleted successfully.");
        iUserService.delete(id);
        return new ResponseEntity<>(messageError, HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('Responsable')")
    public ResponseEntity<UserDto> getByName(@RequestParam String name)
    {
        try{
            UserDto user = iUserService.getByName(name);
            return  new ResponseEntity<>(user, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

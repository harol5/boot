package com.example.boot.controller;

import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.example.boot.exceptions.AuthenticationFailed;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.boot.entities.Team;
import com.example.boot.service.TeamService;
import com.example.boot.exceptions.EntityNotFound;

@RestController // this tells Spring we are using this class as a controller to handle http requests & responses
public class TeamController {
    private static Logger teamLogger = LoggerFactory.getLogger(TeamController.class);
    @Autowired
    private TeamService teamService;
    /*
     * Use exception handling anytime you have a potential recurring issue
     * like a non-existent entity being referenced
     */
    @ExceptionHandler(AuthenticationFailed.class)
    public ResponseEntity<String> authenticationFailed(AuthenticationFailed e){
        teamLogger.error(e.getLocalizedMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<String> entityNotFound(EntityNotFound e){
        teamLogger.error(e.getLocalizedMessage(),e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<String> sqlIssue(PSQLException e){
        teamLogger.error(e.getLocalizedMessage(),e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> deleteFailed(EmptyResultDataAccessException e){
        teamLogger.error(e.getLocalizedMessage(),e);
        return new ResponseEntity<>("could not delete team", HttpStatus.BAD_REQUEST);
    }

    //-----------------------------------------------------------------------------------------

    @GetMapping("/api/team/id/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable int id){
        Team team = this.teamService.getTeamById(id); // getTeamById will return a team for us
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @GetMapping("/api/team/{name}")
    public ResponseEntity<Team> getTeamByName(@PathVariable String name){
        Team team = this.teamService.getTeamByName(name);
        return new ResponseEntity<>(team, HttpStatus.OK); // return the team with a success message if we got data
    }

    @GetMapping("/api/team")
    public ResponseEntity<List<Team>> getAllTeams(){
        List<Team> teams = this.teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @PostMapping("/api/team")
    public ResponseEntity<String> createTeam(@RequestBody Team team){
        String message = this.teamService.createTeam(team.getTeamName());
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PatchMapping("/api/team")
    public ResponseEntity<String> updateTeam(@RequestBody Team team){
        String message = this.teamService.updateTeam(team.getTeamName(), team.getTeamId());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/api/team/{id}")
    public ResponseEntity<String> deleteTeamById(@PathVariable int id){
        String message = this.teamService.deleteTeam(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

package net.engineeringdigest.journalApp.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{UserName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String UserName) {
        User user = userService.findByUserName(UserName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        try {
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String myId) {
        try {
            ObjectId objectId = new ObjectId(myId); // Convert String to ObjectId
            Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);
            return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) { // Catch invalid ObjectId format
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deletejJournalEntry(@PathVariable ObjectId myId, @PathVariable String userName) {
        journalEntryService.deleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalEntry(
            @PathVariable String myId,
            @RequestBody JournalEntry newEntry,
            @PathVariable String userName) {
        try {
            ObjectId objectId = new ObjectId(myId);
            JournalEntry oldEntry = journalEntryService.findById(objectId).orElse(null);

            if (oldEntry != null) {
                // Update title if new one is provided
                if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
                    oldEntry.setTitle(newEntry.getTitle());
                }

                // Update content if new one is provided
                if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
                    oldEntry.setContent(newEntry.getContent());
                }

                // Save updated entry
                journalEntryService.saveEntry(oldEntry);

                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Journal entry not found", HttpStatus.NOT_FOUND);
            }

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid ObjectId format", HttpStatus.BAD_REQUEST);
        }
    }

}


//journalApp/src/main/java/net/engineeringdigest/journalApp/controller/JournalEntryController.java

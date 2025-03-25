package net.engineeringdigest.journalApp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;



@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll() {
      return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry) {
        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry);
        return myEntry;
    }

    @GetMapping("id/{myId}") 
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId) {
        return journalEntryService.findById(myId).orElse(null);
    }

    @DeleteMapping("id/{myId}")
    public boolean deletejJournalEntry(@PathVariable ObjectId myId) {
        journalEntryService.deleteById(myId);
        return true;
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateJournalEntry(@PathVariable String id, @RequestBody JournalEntry myEntry) {
        JournalEntry oldEntry = journalEntryService.findById(new ObjectId(id)).orElse(null);
        if (oldEntry != null) {
            if (myEntry.getTitle() != null && !myEntry.getTitle().isEmpty()) {
                oldEntry.setTitle(myEntry.getTitle());
            }
            if (myEntry.getContent() != null && !myEntry.getContent().isEmpty()) {
                oldEntry.setContent(myEntry.getContent());
            }
            journalEntryService.saveEntry(oldEntry);
        }
        return oldEntry;
    }

}






//journalApp/src/main/java/net/engineeringdigest/journalApp/controller/JournalEntryController.java
//journalApp/src/main/java/net/engineeringdigest/journalApp/entity.java
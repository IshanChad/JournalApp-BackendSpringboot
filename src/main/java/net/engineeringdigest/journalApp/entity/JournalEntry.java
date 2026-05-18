package net.engineeringdigest.journalApp.entity;

import lombok.*;
import net.engineeringdigest.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

// so an instance of JournalEntry will be equal to a document (row)
@Document(collection = "journal_entries") // to map it to a collection as studied in Object Relational Mapping
@Data // during compile time all necessary getters,setters,constructors,hashcode will get added
@NoArgsConstructor
public class JournalEntry {

    @Id //to map id as primary key
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;
}

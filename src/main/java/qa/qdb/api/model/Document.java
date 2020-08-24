package qa.qdb.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type;

    @JsonIgnore
    @Lob
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    private Long postId;

}

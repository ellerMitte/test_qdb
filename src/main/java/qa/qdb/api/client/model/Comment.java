package qa.qdb.api.client.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment {
    private Long id;
    private Long postId;
    private String name;
    private String email;
    private String body;
}

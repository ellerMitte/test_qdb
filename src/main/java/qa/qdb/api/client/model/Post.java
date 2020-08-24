package qa.qdb.api.client.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String body;
}

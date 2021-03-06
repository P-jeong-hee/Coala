package com.clone.finalProject.dto.chatMessageDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class ChatMessageDto  {
    //    public class ChatMessageDto implements Serializable {
//    private static final long serialVersionUID = 6494678977089006639L;

    private String senderName;
    private String message;
    private String status;
    private String area;
    private Long pid;
    private Long uid;
    private String career;
    private LocalDateTime createdAt;
    private String opposingUserName;

    private long userCount;
    private String roomId;

}

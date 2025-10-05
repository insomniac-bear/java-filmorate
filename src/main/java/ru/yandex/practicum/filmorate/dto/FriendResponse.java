package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.util.FriendStatusValues;

@Data
@Builder
public class FriendResponse {
    private Long id;
    private Long userId;
    private Long friendId;
    private FriendStatusValues status;
}

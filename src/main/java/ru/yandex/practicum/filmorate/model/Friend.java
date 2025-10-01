package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.util.FriendStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friend {
    private Long userId;
    private Long friendId;
    private FriendStatus status;
}

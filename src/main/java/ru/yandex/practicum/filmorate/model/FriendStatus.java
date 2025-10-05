package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.util.FriendStatusValues;

@Data
public class FriendStatus {
    Long id;
    FriendStatusValues status;
}

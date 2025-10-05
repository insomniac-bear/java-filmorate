package ru.yandex.practicum.filmorate.sevice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.FriendResponse;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcFriendRepository;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;
import ru.yandex.practicum.filmorate.util.FriendStatusValues;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final JdbcUserRepository userRepository;
    private final JdbcFriendRepository friendRepository;

    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::userToUserResponse)
                .toList();
    }

    public UserResponse getById(Long userId) {
        if (userId == null) {
            log.error("Не передан id пользователя");
            throw new ValidationException("userId не передан");
        }

        return userRepository.findById(userId)
                .map(UserMapper::userToUserResponse)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", userId);
                    return new NotFoundException("Пользователь с id: " + userId + " не найден");
                });
    }

    public UserResponse create(CreateUserRequest request) {
        User user = UserMapper.createUserRequestToUser(request);
        User createdUser = userRepository.create(user);
        return UserMapper.userToUserResponse(createdUser);
    }

    public UserResponse update(UpdateUserRequest request) {
        if (!request.hasId()) {
            log.error("Id является обязательным");
            throw new IllegalArgumentException("Id является обязательным");
        }

        User oldUser = userRepository.findById(request.getId())
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", request.getId());
                    return new NotFoundException("Пользователь не найден");
                });
        User updatedUser = userRepository.update(oldUser);
        return UserMapper.userToUserResponse(updatedUser);
    }

    public UserResponse addFriend(Long userId, Long friendId) {
        UserResponse user = getById(userId);
        UserResponse friend = getById(friendId);
        Optional<FriendResponse> friendInfo = friendRepository.getFriendById(userId, friendId);

        if (friendInfo.isPresent()) {
            log.info("Повторная попытка добавления пользователя {} в друзья к пользователю {}", friend, user);
            throw new IllegalArgumentException("Пользователь уже добавлен в друзья");
        }

        Optional<FriendResponse> friendQuery = friendRepository.getFriendById(friendId, userId);

        if (friendQuery.isEmpty()) {
            friendRepository.addFriend(userId, friendId);
            log.info("Пользователь {} добавлен в друзья к пользователю {}", friend, user);
        } else {
            friendRepository.acceptFriend(userId, friendId);
            log.info("Пользователь {} подтвердил дружбу с пользователем {}", friend, user);
        }

        return user;
    }

    public void removeFriend(Long userId, Long friendId) {
        UserResponse user = getById(userId);
        UserResponse friendUser = getById(friendId);
        Optional<FriendResponse> friend = friendRepository.getFriendById(userId, friendId);

        if (friend.isPresent()) {
            FriendResponse friendData = friend.get();
            if (friendData.getStatus() == FriendStatusValues.ACCEPTED) {
                friendRepository.declineFriend(userId, friendId);
                log.info("Пользователь {} отменил дружбу с пользователем {}", friendUser, user);
            } else {
                friendRepository.removeFriend(userId, friendId);
                log.info("Пользователь {} удалил заявку на дружбу с пользователем {}", friendUser, user);
            }
        }
    }

    public List<UserResponse> getUserFriends(Long userId) {
        UserResponse user = getById(userId);
        Set<Long> friends = friendRepository.getAllFriends(user.getId());
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(friend -> friends.contains(friend.getId()))
                .map(UserMapper::userToUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getCommonFriends(Long userId, Long friendId) {
        Set<Long> userFriends = friendRepository.getAllFriends(userId);
        Set<Long> otherFriends = friendRepository.getAllFriends(friendId);

        Set<Long> commonFriendIds = new HashSet<>(userFriends);
        commonFriendIds.retainAll(otherFriends);
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(anyUser -> commonFriendIds.contains(anyUser.getId()))
                .map(UserMapper::userToUserResponse)
                .collect(Collectors.toList());
    }
}

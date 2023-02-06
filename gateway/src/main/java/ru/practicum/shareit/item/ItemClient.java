package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemDTO itemDto, Integer userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> getItem(Integer itemId, Integer userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> putItem(ItemDTO itemDto, Integer userId) {
        return put("", userId, itemDto);
    }

    public ResponseEntity<Object> deleteItem(Integer itemId, Integer userId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> findAllItems(Integer userId, Integer from,
                                               Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchItems(String text, Integer userId, Integer from,
                                               Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> updateItem(Integer itemId, ItemUpdateDTO dto, Integer userId) {
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> addComment(CommentDTO commentDto, Integer itemId, Integer userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}

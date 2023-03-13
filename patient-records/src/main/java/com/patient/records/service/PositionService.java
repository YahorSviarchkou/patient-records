package com.patient.records.service;

import com.patient.records.entity.Position;
import com.patient.records.repository.PositionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PositionService {

    PositionRepository positionRepository;

    public List<Position> getAllPositions() {
        var positionList = positionRepository.findAll();
        return positionList.isEmpty()
                ? Collections.emptyList()
                : positionList;
    }

    public Position deliverPosition(@NotNull Position position) {
        var positionDb = positionRepository.findByTitle(prepareTitle(position.getTitle()));
        return positionDb.orElseGet(() -> createPosition(position));
    }

    public Position createPosition(@NotNull Position position) {
        if (Objects.nonNull(position.getId())) {
            throw new IllegalArgumentException("Position title must be null on creation");
        }
        position.setTitle(prepareTitle(position.getTitle()));
        return positionRepository.save(position);
    }

    public void deletePosition(@NotNull Long id) {
        positionRepository.deleteById(id);
    }

    private String prepareTitle(@NotBlank String title) {
        var result = title.toLowerCase(Locale.ROOT);
        return result.substring(0, 1).toUpperCase(Locale.ROOT) + result.substring(1);
    }
}

package com.patient.records.controller.image;

import com.patient.records.service.image.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "images", description = "images documentation")
public class ImageController {

    ImageService imageService;

    @Operation(
            operationId = "uploadFile",
            summary = "upload file by patient id",
            tags = {"images"}
    )
    @PostMapping("/upload/{patientId}")
    @PreAuthorize("hasAuthority('LOCAL_ADMIN')")
    public ResponseEntity<ResponseImageDetails> uploadFile(@PathVariable("patientId") Long id,
                                                           @RequestParam("file") MultipartFile multipartFile) {
        imageService.saveFile(id, multipartFile);

        ResponseImageDetails details =
                new ResponseImageDetails("file: " + multipartFile.getName() + " uploaded", LocalDate.now());
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @Operation(
            operationId = "getFile",
            summary = "get file by patient id",
            tags = {"images"}
    )
    @GetMapping("/files/{patientId}")
    @PreAuthorize("hasAnyAuthority('LOCAL_ADMIN', 'CHIEF_PHYSICIAN', 'PHYSICIAN')")
    public ResponseEntity<Resource> getFile(@PathVariable("patientId") Long id) {
        Resource file = imageService.getFile(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}

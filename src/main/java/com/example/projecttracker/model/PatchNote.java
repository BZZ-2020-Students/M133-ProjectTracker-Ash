package com.example.projecttracker.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

import static com.example.projecttracker.util.Constants.*;

/**
 * A patch note that is added to a project. Preferably, this is a short note that describes the changes made to the project.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-20
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatchNote {
    /**
     * The uuid of the patch note.
     *
     * @since 1.0
     */
    private String patchNoteUUID = UUID.randomUUID().toString();
    /**
     * The title of the patch note.
     *
     * @since 1.0
     */
    @FormParam("title")
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    private String title;
    /**
     * The description of the patch note.
     *
     * @since 1.0
     */
    @FormParam("description")
    @Size(max = MAX_DESCRIPTION_LENGTH)
    private String description;
    /**
     * The date the patch note was created.
     *
     * @since 1.0
     */
    private LocalDate date = LocalDate.now();
    /**
     * The version number of the patch note.
     *
     * @since 1.0
     */
    @FormParam("version")
    @NotEmpty(message = "Version cannot be empty")
    @Size(min = MIN_VERSION_LENGTH, max = MAX_VERSION_LENGTH)
    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatchNote patchNote = (PatchNote) o;
        return patchNoteUUID.equals(patchNote.patchNoteUUID) && title.equals(patchNote.title) && description.equals(patchNote.description) && date.equals(patchNote.date) && version.equals(patchNote.version);
    }
}

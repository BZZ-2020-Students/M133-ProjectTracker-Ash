package com.example.projecttracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

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
public class PatchNote {
    /**
     * The uuid of the patch note.
     *
     * @since 1.0
     */
    private String patchNoteUUID;
    /**
     * The title of the patch note.
     *
     * @since 1.0
     */
    private String title;
    /**
     * The description of the patch note.
     *
     * @since 1.0
     */
    private String description;
    /**
     * The date the patch note was created.
     *
     * @since 1.0
     */
    private LocalDate date;
    /**
     * The version number of the patch note.
     *
     * @since 1.0
     */
    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatchNote patchNote = (PatchNote) o;
        return patchNoteUUID.equals(patchNote.patchNoteUUID) && title.equals(patchNote.title) && description.equals(patchNote.description) && date.equals(patchNote.date) && version.equals(patchNote.version);
    }
}

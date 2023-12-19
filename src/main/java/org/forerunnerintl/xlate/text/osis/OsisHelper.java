package org.forerunnerintl.xlate.text.osis;

import org.forerunnerintl.xlate.text.DocumentNoteType;
import org.forerunnerintl.xlate.text.VerseReference;

public class OsisHelper {
    public static OsisVerse getVerse(OsisDocument document, VerseReference verseReference) {
        OsisVerse result = null;

        String chapterId = verseReference.toChapterId();
        OsisChapter chapter = null;
        for (OsisChapter oc : document.getOsisText().getOsisBook().getOsisChapters()) {
            if (chapterId.equals(oc.getOsisId())) {
                chapter = oc;
                break;
            }
        }

        if (chapter != null) {
            String verseId = verseReference.toVerseId();
            for (OsisVerse ov : chapter.getOsisVerses()) {
                if (verseId.equals(ov.getUniqueId())) {
                    result = ov;
                    break;
                }
            }
        }

        return result;
    }

    public static VerseReference getVerseRef(OsisVerse verse) {
        String osisId = verse.getUniqueId();

        String[] parts = osisId.split("\\.");
        int verseNumber = Integer.parseInt(parts[2]);
        VerseReference result = new VerseReference(parts[0], parts[1], verseNumber);

        return result;
    }

    public static OsisNote getNote(OsisVerse verse, String noteId) {
        OsisNote result = null;

        for (OsisNote osisNote : verse.getOsisNotes()) {
            if (osisNote.getNoteId().equals(noteId)) {
                result = osisNote;
                break;
            }
        }

        return result;
    }

    public static OsisNote getNote(OsisVerse verse, DocumentNoteType noteType) {
        OsisNote result = null;

        for (OsisNote osisNote : verse.getOsisNotes()) {
            if (osisNote.getNoteId() == null || osisNote.getNoteId().equals("")) {
                if (noteType == osisNote.getType()) {
                    result = osisNote;
                    break;
                }
            }
        }

        return result;
    }
}

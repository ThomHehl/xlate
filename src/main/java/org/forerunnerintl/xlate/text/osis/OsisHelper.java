package org.forerunnerintl.xlate.text.osis;

import org.forerunnerintl.xlate.text.DocumentNoteType;
import org.forerunnerintl.xlate.text.VerseReference;

public class OsisHelper {
    public static final String      ID_SEPARATOR = "\\.";
    public static final String      QUOTE_SEPARATOR = " -- ";
    public static final String      VAR_NOTE_REF = "$noteReference$";

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

        String[] parts = osisId.split(ID_SEPARATOR);
        int verseNumber = Integer.parseInt(parts[2]);
        VerseReference result = new VerseReference(parts[0], parts[1], verseNumber);

        return result;
    }

    public static OsisNote getNote(OsisVerse verse, String noteId) {
        OsisNote result = null;

        for (OsisNote osisNote : verse.getOsisNotes()) {
            String nid = osisNote.getNoteId();
            if (nid != null && nid.equals(noteId)) {
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

    public static String getVerseText(OsisDocument document, VerseReference verseReference) {
        OsisVerse verse = getVerse(document, verseReference);
        StringBuilder sb = new StringBuilder(getVerseText(verse, null));

        sb.append(QUOTE_SEPARATOR);
        sb.append(verseReference);

        String result = sb.toString().trim();
        return result;
    }

    public static int getChapterNum(OsisChapter chapter) {
        String osisId = chapter.getOsisId();;
        String[] parts = osisId.split(ID_SEPARATOR);
        String str = parts[1];
        int result = Integer.parseInt(str);
        return  result;
    }

    /**
     *  Get the text of the verse
     * @param verse the verse
     * @param template a template for the note reference or null if no note references. Use #VAR_NOTE_REF for the replacement
     *                 variable.
     * @return the verse text
     */
    public static String getVerseText(OsisVerse verse, String template) {
        boolean trackingNoteRefs = template != null;
        StringBuilder sb = new StringBuilder();

        for (OsisWord word : verse.getOsisWords()) {
            sb.append(word.getBodyText());
            String noteId = word.getNoteId();
            if (trackingNoteRefs && noteId != null) {
                sb.append(template.replace(VAR_NOTE_REF, noteId));
            }
            sb.append(' ');
        }

        String result = sb.toString().trim();
        return  result;
    }
}

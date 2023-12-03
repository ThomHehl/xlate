package org.forerunnerintl.xlate.ui;

import com.heavyweightsoftware.util.Pair;
import org.forerunnerintl.xlate.controller.EditorController;
import org.forerunnerintl.xlate.controller.EditorControllerWrapper;
import org.forerunnerintl.xlate.io.ProjectSettings;
import org.forerunnerintl.xlate.text.osis.OsisChapter;
import org.forerunnerintl.xlate.text.osis.OsisDocument;
import org.forerunnerintl.xlate.text.osis.OsisVerse;
import org.forerunnerintl.xlate.text.osis.OsisWord;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainEditorPane extends JPanel {
    private static final int    ANSWER_YES = 0;

    public static final String  NAME_DEFINITION = "Definition";
    public static final String  NAME_TRANSLATION_NOTE = "Translation Note";
    public static final String  RICH_TEXT = "text/rtf";
    public static final String  STYLE_BODY_TEXT = "bodyText";
    public static final String  STYLE_VERSE_NUMBER = "verseNumber";

    private List<Pair<String, String>>
                            bookList;
    private OsisDocument    document;
    private boolean         documentDirty;
    private final JFrame    owner;

    private Style           bodyText;
    private DefaultStyledDocument
                            styledDocument;
    private Map<Integer, OsisWord>
                            wordsByOffset = new HashMap<>();
    private Style           verseNumber;

    private JEditorPane     editMainEditor;
    private JScrollPane     scrollMainEditor;
    private JTabbedPane     tabbedPane;

    private JPanel          panelBookSelection;
    private JComboBox<String>
                            cmbBookSelection;
    private JComboBox<String>
                            cmbChapterSelection;
    private JButton         buttonEditDefinition;
    private JLabel          labelStatusBar;
    private JPanel          panelDefinition;
    private JScrollPane     scrollDefinition;
    private JTextArea       textDefinition;

    private JButton         buttonEditTranslation;
    private JPanel          panelTranslation;
    private JScrollPane     scrollTranslation;
    private JTextArea       textTranslation;

    private boolean         booksLoaded;
    private EditorController
                            editorController;

    public MainEditorPane(JFrame owner) {
        super(new BorderLayout());
        this.owner = owner;
        editorController = new EditorControllerWrapper(this);

        buildBookSelectionPane();
        buildEditorPane();
        buildTabPane();
        buildStatusBar();
    }

    private void buildBookSelectionPane() {
        panelBookSelection = new JPanel(new GridLayout(1, 2));

        cmbBookSelection = new JComboBox<>();
        cmbBookSelection.setPreferredSize(UiConstants.COMBO_SIZE);
        panelBookSelection.add(cmbBookSelection);

        cmbChapterSelection = new JComboBox<>();
        cmbChapterSelection.setPreferredSize(UiConstants.COMBO_SIZE);
        cmbChapterSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String chapter = (String) cmbChapterSelection.getSelectedItem();
                chapterSelected(chapter);
            }
        });
        panelBookSelection.add(cmbChapterSelection);

        add(panelBookSelection, BorderLayout.NORTH);
    }

    private void bookSelected(String bookName) {
        System.err.println("Book selected:" + bookName);
        String bookCode = getBookCode(bookName);
        Thread.yield();
        editorController.loadBook(bookCode);
    }

    private String getBookCode(String bookName) {
        String result = null;

        for (Pair<String, String> bookEntry : bookList) {
            if (bookName.equals(bookEntry.getRight())) {
                result = bookEntry.getLeft();
                break;
            }
        }

        return result;
    }

    private void chapterSelected(String chapter) {
    }

    private void buildEditorPane() {
        editMainEditor = new JEditorPane(RICH_TEXT, "");
        editMainEditor.setEditable(false);

        buildStyledDocument();

        scrollMainEditor = new JScrollPane(editMainEditor);
        add(scrollMainEditor, BorderLayout.WEST);
    }

    private void buildStyledDocument() {
        // Create the StyleContext, and the document
        StyleContext sc = new StyleContext();
        styledDocument = new DefaultStyledDocument(sc);
        editMainEditor.setDocument(styledDocument);

        // Create and add the main document style
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        bodyText = sc.addStyle(STYLE_BODY_TEXT, defaultStyle);
        StyleConstants.setLeftIndent(bodyText, 5);
        StyleConstants.setRightIndent(bodyText, 5);
        StyleConstants.setFirstLineIndent(bodyText, 10);
        StyleConstants.setFontFamily(bodyText, "serif");
        StyleConstants.setFontSize(bodyText, 12);

        // Create and add the verse number style
        verseNumber = sc.addStyle(STYLE_VERSE_NUMBER, bodyText );
        StyleConstants.setFontSize(verseNumber, 14);
        StyleConstants.setBold(verseNumber, true);
    }

    private void buildStatusBar() {
        labelStatusBar = new JLabel();
        labelStatusBar.setPreferredSize(UiConstants.STATUS_BAR_SIZE);
        add(labelStatusBar, BorderLayout.SOUTH);
    }

    private void buildTabPane() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.EAST);

        addDefinitionTab();
        addTranslationNoteTab();
    }

    private void addDefinitionTab() {
        panelDefinition = new JPanel(new BorderLayout());
        panelDefinition.setName(NAME_DEFINITION);

        scrollDefinition = new JScrollPane();
        panelDefinition.add(scrollDefinition, BorderLayout.NORTH);

        textDefinition = new JTextArea();
        scrollDefinition.add(textDefinition);

        buttonEditDefinition = new JButton("Edit...");
        buttonEditDefinition.setPreferredSize(UiConstants.BUTTON_SIZE);
        panelDefinition.add(buttonEditDefinition, BorderLayout.SOUTH);

        tabbedPane.add(panelDefinition);
    }

    private void addTranslationNoteTab() {
        panelTranslation = new JPanel(new BorderLayout());
        panelTranslation.setName(NAME_TRANSLATION_NOTE);

        scrollTranslation = new JScrollPane();
        panelTranslation.add(scrollTranslation, BorderLayout.NORTH);

        textTranslation = new JTextArea();
        scrollTranslation.add(textTranslation);

        buttonEditTranslation = new JButton("Edit...");
        buttonEditTranslation.setPreferredSize(UiConstants.BUTTON_SIZE);
        panelTranslation.add(buttonEditTranslation, BorderLayout.SOUTH);

        tabbedPane.add(panelTranslation);
    }

    public void resizeComponents(int newWidth, int newHeight) {
        int leftWidth = (int) Math.floor(newWidth * .75);
        int rightWidth = newWidth - leftWidth;

        Dimension leftDim = new Dimension(leftWidth, newHeight);
        scrollMainEditor.setPreferredSize(leftDim);

        Dimension rightDim = new Dimension(rightWidth, newHeight);
        tabbedPane.setPreferredSize(rightDim);
    }

    public void openProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select bible project directory");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            editorController.openProjectDirectory(dir);
        }
    }

    /**
     * A project wasn't found for the specified directory. Handle it.
     * @param projectDir the project directory
     */
    public void handleNoProject(File projectDir) {
        Object[] buttonText = {"Yes", "No"};
        if (showYesNoDialog(buttonText,
                "No project found in: " + projectDir.getAbsolutePath() + ". Create new project?",
                "No Project Found")) {
            editorController.createProject(projectDir);
        }
    }

    private boolean showYesNoDialog(Object[] buttonText, String msg, String title) {
        int choice = JOptionPane.showOptionDialog(this,
                msg,
                title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                buttonText,
                buttonText[0]);

        return choice == ANSWER_YES;
    }

    public void newProject(ProjectSettings projectSettings) {
        NewProjectDialog dialog = new NewProjectDialog(owner);
        if (dialog.isCreating()) {
            projectSettings.setTitle(dialog.getTitle());
            projectSettings.setOldTestamentSourceFormat(dialog.getOldTestament());
            projectSettings.setNewTestamentSourceFormat(dialog.getNewTestament());
            projectSettings.store();

            editorController.convertSource(projectSettings);
        }
    }

    /**
     * Report a directory not found
     * @param msg the message to display
     * @param path the name of the missing directory
     */
    public void directoryNotFound(String msg, String path) {
        String fullMessage = msg + ": " + path;
        showError(msg, fullMessage);
    }

    private void showError(String title, String msg) {
        JOptionPane.showMessageDialog(this,
                msg,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    public void setStatusText(String text) {
        labelStatusBar.setText(text);
    }

    public void setBooks(List<Pair<String, String>> books) {
        this.bookList = books;

        while (cmbBookSelection == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.err.println(ie.toString());
            }
        }
        cmbBookSelection.removeAllItems();
        for (Pair<String, String> bookEntry : bookList) {
            cmbBookSelection.addItem(bookEntry.getRight());
        }

        if (!booksLoaded) {
            cmbBookSelection.addActionListener(event -> {
                String bookName = (String) cmbBookSelection.getSelectedItem();
                bookSelected(bookName);
            });

            booksLoaded = true;
        }
    }

    private void setChapters(int numChapters) {
        while (cmbChapterSelection == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        cmbChapterSelection.removeAllItems();

        for (int ix = 1; ix <= numChapters; ++ix) {
            String str = Integer.toString(ix);
            cmbChapterSelection.addItem(str);
        }
    }

    /**
     * Set the book to the current reference
     * @param book the code for the book
     * @param chapter the chapter. If null, set to chapter 1.
     */
    public void setCurrentReference(String book, String chapter) {
        editorController.loadBook(book);
    }

    public void setOsisDocument(OsisDocument osisDocument) {
        this.document = osisDocument;
        List<OsisChapter> chapters = document.getOsisText().getOsisBook().getOsisChapters();
        setChapters(chapters.size());
        setChapterText(chapters.get(0));
        setDocumentDirty(false);
    }

    public boolean isDocumentDirty() {
        return documentDirty;
    }

    public void setDocumentDirty(boolean documentDirty) {
        this.documentDirty = documentDirty;
    }

    private void setChapterText(OsisChapter osisChapter) {
        editMainEditor.setText("");
        List<OsisVerse> verseList = osisChapter.getOsisVerses();
        List<VerseData> verseDataList = new ArrayList<>(verseList.size());
        StringBuilder sb = new StringBuilder();

        for (int ix = 0; ix < verseList.size(); ++ix) {
            int numStart = sb.length();
            String verseNum = Integer.toString(ix + 1);
            sb.append(verseNum);
            sb.append(" ");

            OsisVerse verse = verseList.get(ix);
            for(OsisWord word : verse.getOsisWords()) {
                int offset = sb.length();
                wordsByOffset.put(offset, word);
                String text = word.getBodyText();
                sb.append(text);
                sb.append(" ");

                VerseData data = new VerseData(verseNum, numStart);
                verseDataList.add(data);
            }

            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        setStyledText(sb.toString(), verseDataList);
    }

    private void setStyledText(String text, List<VerseData> verseDataList) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    // Set the logical style
                    styledDocument.setLogicalStyle(0, bodyText);

                    // Add the text to the document
                    styledDocument.insertString(0, text, null);

                    for (VerseData verseData : verseDataList) {
                        styledDocument.setCharacterAttributes(verseData.indexOffset, verseData.indexText.length(), verseNumber, true);
                    }

                    // Finally, apply the style to the heading
                    styledDocument.setParagraphAttributes(0, 1, verseNumber, false);
                } catch (BadLocationException bde) {
                    bde.printStackTrace();
                }
            });
        } catch (Throwable tw) {
            System.out.println("Exception when constructing document: " + tw);
            System.exit(1);
        }
    }

    protected record VerseData(String indexText, int indexOffset) { }
}

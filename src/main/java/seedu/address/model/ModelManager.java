package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    //private final ObservableList<Lesson> lessons;
    private final FilteredList<Lesson> filteredLessons;
    private ConfirmableCommand pendingCommand = null;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.versionedAddressBook = new VersionedAddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.versionedAddressBook.getPersonList());
        //this.lessons = FXCollections.observableArrayList();
        this.filteredLessons = new FilteredList<>(this.versionedAddressBook.getLessonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== Undo/Redo Logic ================================================================================


    @Override
    public void commitAddressBook(String command) {
        versionedAddressBook.commit(command);
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    @Override
    public void commitAddressBook(String command, seedu.address.logic.commands.CommandResult.DisplayType displayType) {
        versionedAddressBook.commit(command, displayType);
    }

    @Override
    public String getLastCommandDescription() {
        return versionedAddressBook.getLastCommandDescription();
    }

    @Override
    public seedu.address.logic.commands.CommandResult.DisplayType getLastCommandDisplayType() {
        return versionedAddressBook.getLastCommandDisplayType();
    }

    @Override
    public String getRedoCommandDescription() {
        return versionedAddressBook.getRedoCommandDescription();
    }

    @Override
    public seedu.address.logic.commands.CommandResult.DisplayType getRedoCommandDisplayType() {
        return versionedAddressBook.getRedoCommandDisplayType();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
    }

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.versionedAddressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    // Person operations

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedAddressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    // Lesson operations

    @Override
    public boolean hasLesson(Lesson lesson) {
        requireNonNull(lesson);
        return versionedAddressBook.hasLesson(lesson);
    }

    @Override
    public void deleteLesson(Lesson target) {
        versionedAddressBook.removeLesson(target);
    }

    @Override
    public void addLesson(Lesson lesson) {
        requireNonNull(lesson);
        versionedAddressBook.addLesson(lesson);
        updateFilteredLessonList(PREDICATE_SHOW_ALL_LESSONS);
    }

    @Override
    public void setLesson(Lesson target, Lesson editedLesson) {
        requireAllNonNull(target, editedLesson);
        versionedAddressBook.setLesson(target, editedLesson);
    }

    @Override
    public ObservableList<Lesson> getFilteredLessonList() {
        return filteredLessons;
    }

    @Override
    public void updateFilteredLessonList(Predicate<Lesson> predicate) {
        requireNonNull(predicate);
        filteredLessons.setPredicate(predicate);
    }

    @Override
    public ConfirmableCommand getPendingCommand() {
        return pendingCommand;
    }

    @Override
    public void updatePendingCommand(ConfirmableCommand command) {
        this.pendingCommand = command;
    }

    @Override
    public void clearPendingCommand() {
        this.pendingCommand = null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return versionedAddressBook.equals(otherModelManager.versionedAddressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && getFilteredPersonList().equals(otherModelManager.getFilteredPersonList());
    }

}

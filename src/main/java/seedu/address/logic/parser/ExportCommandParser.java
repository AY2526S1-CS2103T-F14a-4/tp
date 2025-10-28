package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        try {
            Path filePath = Paths.get(trimmedArgs);

            // Check file extension
            String fileName = filePath.getFileName().toString().toLowerCase();
            if (!fileName.endsWith(".json")) {
                throw new ParseException("File must be a .json file.");
            }

            return new ExportCommand(filePath);
        } catch (InvalidPathException ipe) {
            throw new ParseException("Invalid file path provided: " + trimmedArgs, ipe);
        }
    }
}
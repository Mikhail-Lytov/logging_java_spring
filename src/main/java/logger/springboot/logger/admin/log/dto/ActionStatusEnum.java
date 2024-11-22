package logger.springboot.logger.admin.log.dto;

/**
 * <p>
 * The {@code ActionStatusEnum} enum represents different action statuses that can be associated with a method
 * for logging purposes.
 *
 * <p>The enum provides the following action statuses:</p>
 * <ul>
 *     <li>{@link #SUCCESSFULLY}: Action completed successfully</li>
 *     <li>{@link #FAILED}: Action failed</li>
 *     <li>{@link #ERROR}: Action encountered an error</li>
 *     <li>{@link #UNKNOWN}: Unknown action status</li>
 * </ul>
 *
 */
public enum ActionStatusEnum {
    SUCCESSFULLY,
    FAILED,
    ERROR,
    UNKNOWN
}

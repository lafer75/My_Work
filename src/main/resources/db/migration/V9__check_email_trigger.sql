CREATE OR REPLACE FUNCTION check_email_not_null()
    RETURNS TRIGGER
AS
$$
BEGIN
    IF NEW.email IS NULL THEN
        RAISE EXCEPTION 'Email cannot be null';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_email_not_null
    BEFORE INSERT OR UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION check_email_not_null();

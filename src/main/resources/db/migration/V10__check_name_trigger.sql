CREATE OR REPLACE FUNCTION check_lastname_not_putin()
    RETURNS TRIGGER
AS
$$
BEGIN
    IF NEW.lastname = 'Putin' THEN
        RAISE EXCEPTION 'Lastname cannot be "Putin"';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_lastname_not_putin
    BEFORE INSERT OR UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION check_lastname_not_putin();

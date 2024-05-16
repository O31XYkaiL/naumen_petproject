-- Add the column if it does not exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM   information_schema.columns
        WHERE  table_name = 'block1_project'
        AND    column_name = 'project_rating'
    ) THEN
        ALTER TABLE block1_project ADD COLUMN project_rating INT DEFAULT 0;
    END IF;
END $$;

-- Update existing rows to set project_rating to 0 where it is currently NULL
UPDATE block1_project SET project_rating = 0 WHERE project_rating IS NULL;

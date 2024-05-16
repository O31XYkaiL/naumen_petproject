DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM   information_schema.columns
        WHERE  table_name = 'block1_project'
        AND    column_name = 'project_creator'
    ) THEN
        ALTER TABLE block1_project ADD COLUMN project_creator TEXT DEFAULT 'Unknown';
    END IF;
END $$;

UPDATE block1_project SET project_creator = 'Unknown' WHERE project_creator IS NULL;

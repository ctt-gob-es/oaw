CREATE VIEW oaw_dashboard
AS SELECT id, usr, domain AS url, email, depth, width, report, date, status, analysis_type, in_directory
FROM basic_service

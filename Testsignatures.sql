-- Fake test signatures for the demo. Run after DatabaseTables.sql.
-- Real stalkerware indicators can be loaded later from github.com/AssoEchap/stalkerware-indicators
USE ArgusScanner;
 
INSERT INTO Signatures (package_name, cert_hash, type, app_family) VALUES
('com.example.fakestalker', NULL, 'package', 'FakeStalker (test)'),
('com.test.spyapp',         NULL, 'package', 'TestSpy (test)');
 
-- One case and device so scans have somewhere to go (device 1)
INSERT INTO VCase (survivor_ref, opened_at, status) VALUES ('TEST-CASE', NOW(), 'open');
INSERT INTO Device (platform, model, caseID) VALUES ('android', 'emulator', LAST_INSERT_ID());
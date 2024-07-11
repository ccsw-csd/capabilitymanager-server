ALTER TABLE capabilitymanager.staffing ADD CONSTRAINT staffing_unique_saga UNIQUE KEY (SAGA);
ALTER TABLE capabilitymanager.formdata ADD CONSTRAINT formdata_unique_saga UNIQUE KEY (SAGA);

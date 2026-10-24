-- One running and one upcoming promotion, so the promotions app shows every status.
INSERT INTO promotion (content, endDate, startDate, title)
VALUES
  ('Every scooter ride in October costs 25% less.', '2026-10-31 23:59:59', '2026-10-01 00:00:00', 'October Scooter Month'),
  ('Book an electric car for the whole weekend and get Monday free.', '2027-01-31 23:59:59', '2026-12-01 00:00:00', 'Winter Weekend Deal');

INSERT INTO announcement (content, creationDate, title, updateDate)
VALUES
  ('Vehicles can now be found on the map in the employee app.', '2026-10-20 09:00:00', 'Fleet map is live', '2026-10-20 09:00:00');

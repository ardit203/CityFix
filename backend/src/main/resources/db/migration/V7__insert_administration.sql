-- =========================
-- Departments
-- =========================

INSERT INTO departments (name, description)
VALUES ('Roads and Infrastructure',
        'Responsible for roads, sidewalks, potholes, traffic signs, and public infrastructure.'),
       ('Public Lighting', 'Responsible for street lights, lighting poles, and public lighting maintenance.'),
       ('Waste Management', 'Responsible for waste collection, illegal dumping, bins, and public cleanliness.'),
       ('Water and Sewage', 'Responsible for water leaks, sewage problems, drainage, and manholes.'),
       ('Parks and Green Areas', 'Responsible for parks, trees, grass, playgrounds, and public green spaces.'),
       ('Public Safety', 'Responsible for safety-related public issues and emergency municipal coordination.');


-- =========================
-- Municipalities
-- =========================

INSERT INTO municipalities (name, code)
VALUES ('Kumanovo', 'KUM'),
       ('Skopje', 'SKP'),
       ('Tetovo', 'TET'),
       ('Bitola', 'BTL'),
       ('Ohrid', 'OHR'),
       ('Prilep', 'PRL'),
       ('Gostivar', 'GTV'),
       ('Struga', 'STR');


-- =========================
-- Categories
-- =========================

INSERT INTO categories (name, description, department_id)
VALUES ('Pothole',
        'Reports related to potholes or damaged road surfaces.',
        (SELECT id FROM departments WHERE name = 'Roads and Infrastructure')),
       ('Damaged Sidewalk',
        'Reports related to broken, blocked, or unsafe sidewalks.',
        (SELECT id FROM departments WHERE name = 'Roads and Infrastructure')),
       ('Damaged Traffic Sign',
        'Reports related to missing, broken, or incorrectly placed traffic signs.',
        (SELECT id FROM departments WHERE name = 'Roads and Infrastructure')),
       ('Broken Street Light',
        'Reports related to non-working or damaged public street lights.',
        (SELECT id FROM departments WHERE name = 'Public Lighting')),
       ('Damaged Lighting Pole',
        'Reports related to damaged or unsafe lighting poles.',
        (SELECT id FROM departments WHERE name = 'Public Lighting')),
       ('Overflowing Trash Bin',
        'Reports related to full or overflowing public trash bins.',
        (SELECT id FROM departments WHERE name = 'Waste Management')),
       ('Illegal Dumping',
        'Reports related to waste dumped in unauthorized public areas.',
        (SELECT id FROM departments WHERE name = 'Waste Management')),
       ('Water Leak',
        'Reports related to visible water leaks in public areas.',
        (SELECT id FROM departments WHERE name = 'Water and Sewage')),
       ('Blocked Drain',
        'Reports related to blocked storm drains or drainage problems.',
        (SELECT id FROM departments WHERE name = 'Water and Sewage')),
       ('Open or Damaged Manhole',
        'Reports related to missing, open, or damaged manhole covers.',
        (SELECT id FROM departments WHERE name = 'Water and Sewage')),
       ('Damaged Playground Equipment',
        'Reports related to broken or unsafe playground equipment.',
        (SELECT id FROM departments WHERE name = 'Parks and Green Areas')),
       ('Fallen Tree or Branch',
        'Reports related to fallen trees, dangerous branches, or tree maintenance.',
        (SELECT id FROM departments WHERE name = 'Parks and Green Areas')),
       ('Damaged Public Bench',
        'Reports related to broken benches or public seating areas.',
        (SELECT id FROM departments WHERE name = 'Parks and Green Areas')),
       ('Unsafe Public Area',
        'Reports related to safety risks in public spaces.',
        (SELECT id FROM departments WHERE name = 'Public Safety'));
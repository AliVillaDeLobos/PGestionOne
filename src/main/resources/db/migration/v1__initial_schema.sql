# Table Structures
CREATE TABLE users (
                       id_user INT AUTO_INCREMENT PRIMARY KEY,
                       user_password VARCHAR(255) NOT NULL,
                       name VARCHAR(50) NOT NULL,
                       paternal_last_name VARCHAR(100) NOT NULL,
                       maternal_last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE
);

# --ENUM('INSERT','UPDATE','DELETE')
# --Record_id lo relacionamos a id que se afectara de la tabla
CREATE TABLE audit (
                       id_audit INT AUTO_INCREMENT PRIMARY KEY,
                       table_name VARCHAR(50) NOT NULL,
                       record_id INT NOT NULL,
                       operation VARCHAR(50) NOT NULL,
                       id_user_created INT NOT NULL,
                       old_data JSON NULL,
                       new_data JSON NULL,
                       created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (id_user_created) REFERENCES users(id_user),

                       INDEX idx_audit_table_record (table_name, record_id),
                       INDEX idx_audit_user (id_user_created),
                       INDEX idx_audit_date (created_date)
);

CREATE TABLE roles (
                       id_role INT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL
);

CREATE TABLE project (
                         id_project INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description TEXT,
                         start_date DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE user_role (
                           id_user_role INT AUTO_INCREMENT PRIMARY KEY,
                           id_user INT NOT NULL,
                           id_role INT NOT NULL,
                           FOREIGN KEY (id_user) REFERENCES users(id_user),
                           FOREIGN KEY (id_role) REFERENCES roles(id_role),
                           UNIQUE KEY uq_user_role(id_user, id_role)
);

CREATE TABLE tasks (
                       id_task INT AUTO_INCREMENT PRIMARY KEY,
                       id_project INT NOT NULL,
                       color VARCHAR(50) NOT NULL,
                       name VARCHAR(50) NOT NULL,
                       status VARCHAR(50) DEFAULT 'PENDING',
                       start_date DATE NOT NULL,
                       end_date DATE NOT NULL,
                       FOREIGN KEY (id_project) REFERENCES project(id_project),
                       UNIQUE KEY color_project(id_project, color),
                       UNIQUE KEY uq_project_name(id_project, name)
);

# ENUM('RESPONSIBLE','SUPPORT','REVIEW')
CREATE TABLE task_assignment (
                                 id_task_assignment INT AUTO_INCREMENT PRIMARY KEY,
                                 id_task INT NOT NULL,
                                 id_user INT NOT NULL,
                                 role VARCHAR(50) NOT NULL,
                                 assigned_date DATE DEFAULT (CURRENT_DATE),
                                 FOREIGN KEY (id_task) REFERENCES tasks(id_task),
                                 FOREIGN KEY (id_user) REFERENCES users(id_user),
                                 UNIQUE KEY uq_task_user (id_task, id_user)
);

CREATE TABLE task_assignment_history (
                                         id_task_assignment_history INT AUTO_INCREMENT PRIMARY KEY,
                                         id_task INT NOT NULL,
                                         id_user_assigned INT NOT NULL,
                                         id_user_assigned_by INT NOT NULL,
                                         action ENUM('ASSIGNED','UNASSIGNED') NOT NULL DEFAULT 'ASSIGNED',
                                         action_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (id_task) REFERENCES tasks(id_task),
                                         FOREIGN KEY (id_user_assigned) REFERENCES users(id_user),
                                         FOREIGN KEY (id_user_assigned_by) REFERENCES users(id_user)
);

CREATE TABLE weeks (
                       id_week INT AUTO_INCREMENT PRIMARY KEY,
                       week_num INT NOT NULL,
                       year INT NOT NULL,
                       start_date DATE NOT NULL,
                       end_date DATE NOT NULL
);

CREATE TABLE days (
                      id_day INT AUTO_INCREMENT PRIMARY KEY,
                      id_week INT NOT NULL,
                      date DATE NOT NULL,
                      day_name VARCHAR(50) NOT NULL,
                      FOREIGN KEY (id_week) REFERENCES weeks(id_week)
);

CREATE TABLE subtasks (
                          id_subtask INT AUTO_INCREMENT PRIMARY KEY,
                          id_task INT NOT NULL,
                          description TEXT,
                          name VARCHAR(100) NOT NULL,
                          status BOOLEAN DEFAULT FALSE NOT NULL,
                          start_date DATE NOT NULL,
                          is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
                          FOREIGN KEY (id_task) REFERENCES tasks(id_task)
);

CREATE TABLE days_subtasks (
                               id_day_subtask INT AUTO_INCREMENT PRIMARY KEY,
                               id_subtask INT NOT NULL,
                               status VARCHAR(50) DEFAULT 'PENDING',
                               id_day INT NOT NULL,
                               FOREIGN KEY (id_subtask) REFERENCES subtasks(id_subtask),
                               FOREIGN KEY (id_day) REFERENCES days(id_day) ON DELETE CASCADE
);

CREATE TABLE subtask_deleted_history (
                                         id_subtask_deleted_history INT AUTO_INCREMENT PRIMARY KEY,
                                         id_subtask INT NOT NULL,
                                         message TEXT NOT NULL,
                                         deleted_date DATE DEFAULT (CURRENT_DATE),
                                         FOREIGN KEY (id_subtask) REFERENCES subtasks(id_subtask)
);

# REECORDAR QUE LA LOGICA PARA NO TENER UN OVERLAPVA EN SPRING AL BUSCARL LOS DATOS
CREATE TABLE days_hours (
                            id_days_hours INT AUTO_INCREMENT PRIMARY KEY,
                            id_day_subtask INT NOT NULL,
                            start_time DATETIME NOT NULL,
                            end_time DATETIME NOT NULL,
                            FOREIGN KEY (id_day_subtask)REFERENCES days_subtasks(id_day_subtask)ON DELETE CASCADE,
                            CHECK (start_time < end_time),
                            UNIQUE(id_day_subtask, start_time, end_time)
);  # --Solo mantiene duplicados exactos

CREATE TABLE user_project (
                              id_user_project INT AUTO_INCREMENT PRIMARY KEY,
                              id_user INT NOT NULL,
                              id_project INT NOT NULL,
                              FOREIGN KEY (id_user) REFERENCES users(id_user),
                              FOREIGN KEY (id_project) REFERENCES project(id_project),
                              UNIQUE KEY uq_user_project (id_user, id_project)
);

CREATE TABLE user_project_role (
                                   id_user_project_role INT AUTO_INCREMENT PRIMARY KEY,
                                   id_user_project INT NOT NULL,
                                   id_role INT NOT NULL,
                                   FOREIGN KEY (id_user_project) REFERENCES user_project(id_user_project),
                                   FOREIGN KEY (id_role) REFERENCES roles(id_role),
                                   UNIQUE KEY uq_user_project_role (id_user_project, id_role)
);
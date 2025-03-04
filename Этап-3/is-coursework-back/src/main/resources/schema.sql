CREATE OR REPLACE FUNCTION get_stat_player(id_pl integer) RETURNS SETOF Stat_Player AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Player WHERE player_id = id_pl;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_stat_match(id_st integer) RETURNS SETOF Stat_Match AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Match WHERE id = id_st;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_stat_match_team(id_st integer) RETURNS SETOF Stat_Match_Team AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Match_Team WHERE id_stat_match_id = id_st;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_stat_match_player(id_st integer) RETURNS SETOF Stat_Player_Match AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Player_Match WHERE id_stat_match_id = id_st;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_stat_team_season(id_st integer) RETURNS SETOF Stat_Team_Season AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Team_Season WHERE id = id_st;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_stat_match_future(id_st integer) RETURNS SETOF Stat_Match_Future AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Match_Future WHERE id = id_st;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_stat_match_team_future(id_st integer) RETURNS SETOF Stat_Team_Future AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Team_Future WHERE id_stat_match_id = id_st;
END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_stat_match_player_future(id_st integer) RETURNS SETOF Stat_Player_Future AS '
BEGIN
RETURN QUERY
SELECT * FROM Stat_Player_Future WHERE id_stat_match_id = id_st;
END;
' LANGUAGE plpgsql;

CREATE INDEX idx_stat_player_match_id_st
    ON Stat_Player_Match(id_stat_match_id);

CREATE INDEX idx_stat_match_team_id_st
    ON Stat_Match_Team(id_stat_match_id);

CREATE INDEX idx_stat_team_future_id_st
    ON Stat_Team_Future(id_stat_match_id);

CREATE INDEX idx_stat_player_future_id_st
    ON Stat_Player_Future(id_stat_match_id);

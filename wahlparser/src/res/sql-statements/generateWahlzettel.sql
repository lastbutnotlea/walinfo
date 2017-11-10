with RECURSIVE unendlichkeit(nummer) as (
  select 1
  union ALL
  select nummer + 1 from unendlichkeit
)
select * from erststimmenergebnisse e, unendlichkeit u
  where u.nummer <= e.anzahl;
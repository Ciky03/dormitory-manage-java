-- Fixed mock data for dorm todo integration.
-- Preconditions:
-- 1. room_student has at least one current room (is_current = 1).
-- 2. The selected room has at least one current student.

set @room_id := (
    select rs.room_id
    from room_student rs
    where rs.is_current = 1
    order by rs.create_time
    limit 1
);

set @creator_student_id := (
    select rs.student_id
    from room_student rs
    where rs.room_id = @room_id
      and rs.is_current = 1
    order by rs.create_time
    limit 1
);

set @assignee_student_id := coalesce((
    select rs.student_id
    from room_student rs
    where rs.room_id = @room_id
      and rs.is_current = 1
    order by rs.create_time
    limit 1 offset 1
), @creator_student_id);

set @completed_student_id := coalesce((
    select rs.student_id
    from room_student rs
    where rs.room_id = @room_id
      and rs.is_current = 1
    order by rs.create_time
    limit 1 offset 2
), @assignee_student_id);

delete from dm_todo_comment where todo_id in ('todo-pending', 'todo-processing', 'todo-completed', 'todo-canceled');
delete from dm_todo where id in ('todo-pending', 'todo-processing', 'todo-completed', 'todo-canceled');

insert into dm_todo (
    id, room_id, title, content, priority, status, assignee_student_id, creator_student_id,
    due_time, start_time, completed_time, completed_by, cancel_reason,
    create_by, create_time, update_by, update_time, delflag
)
values
    (
        'todo-pending',
        @room_id,
        '公共区域值日',
        '今晚 21:00 前完成公共区域清洁',
        2,
        0,
        null,
        @creator_student_id,
        date_add(now(), interval 1 day),
        null,
        null,
        null,
        null,
        'sys-1',
        now(),
        'sys-1',
        now(),
        0
    ),
    (
        'todo-processing',
        @room_id,
        '检查门窗',
        '离寝前检查门窗和空调电源',
        3,
        1,
        @assignee_student_id,
        @creator_student_id,
        date_add(now(), interval 2 hour),
        now(),
        null,
        null,
        null,
        'sys-1',
        now(),
        'sys-1',
        now(),
        0
    ),
    (
        'todo-completed',
        @room_id,
        '采购洗洁精',
        '补充宿舍公共清洁用品',
        1,
        2,
        @completed_student_id,
        @creator_student_id,
        date_sub(now(), interval 2 day),
        date_sub(now(), interval 3 day),
        date_sub(now(), interval 1 day),
        @completed_student_id,
        null,
        'sys-1',
        date_sub(now(), interval 3 day),
        'sys-1',
        date_sub(now(), interval 1 day),
        0
    ),
    (
        'todo-canceled',
        @room_id,
        '周末大扫除',
        '原计划周末统一大扫除',
        2,
        3,
        @assignee_student_id,
        @creator_student_id,
        date_add(now(), interval 3 day),
        null,
        null,
        null,
        '本周课程冲突，改期处理',
        'sys-1',
        now(),
        'sys-1',
        now(),
        0
    );

insert into dm_todo_comment (
    id, todo_id, commenter_student_id, content, create_by, create_time, update_by, update_time, delflag
)
values
    (
        'todo-comment-1',
        'todo-completed',
        @creator_student_id,
        '已买好并放到洗手台下面。',
        'sys-1',
        date_sub(now(), interval 1 day),
        'sys-1',
        date_sub(now(), interval 1 day),
        0
    ),
    (
        'todo-comment-2',
        'todo-completed',
        @completed_student_id,
        '收到，后续不够再补。',
        'sys-1',
        date_sub(now(), interval 12 hour),
        'sys-1',
        date_sub(now(), interval 12 hour),
        0
    );

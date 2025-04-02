// Calendar.tsx
import { useEffect, useState } from 'react';
import { ScheduleXCalendar, useCalendarApp } from "@schedule-x/react";
import { createViewWeek, createViewMonthGrid } from '@schedule-x/calendar';
import '@schedule-x/theme-default/dist/calendar.css';
import { createEventModalPlugin } from '@schedule-x/event-modal';
import { createDragAndDropPlugin } from '@schedule-x/drag-and-drop';
import { createEventsServicePlugin } from '@schedule-x/events-service';
import Container from '@mui/material/Container';
// import { useNavigate } from 'react-router-dom';

// import { AppBar } from '@mui/material';
import MenuBar from './MenuBar'

const Calendar: React.FC = () => {
    // const [value, setValue] = useState(0);
    // const [value] = useState(0);
    // const [showDialog, setShowDialog] = useState(false);
    const eventsService = useState(() => createEventsServicePlugin())[0];
    // const navigate = useNavigate();  // 获取导航方法
    // const handleSave = (eventData: any) => {
    //     console.log('Saved event:', eventData);
    //     setShowDialog(false);
    // };

    const calendar = useCalendarApp({
        views: [
            createViewWeek(),
            createViewMonthGrid()
        ],
        events: [
            {
                id: 1,
                title: 'My new event',
                start: '2025-01-01 00:00',
                end: '2025-01-01 02:00',
                description: 'zoom.us/123456',
                color: '#FF0000'
            }
        ],
        selectedDate: '2025-01-01',
        plugins: [
            createEventModalPlugin(),
            createDragAndDropPlugin(),
            eventsService
        ]
    });

    useEffect(() => {
        eventsService.getAll();
    }, [eventsService]);

    return (
        <div className=' min-h-screen bg-gradient-to-r from-purple-100 via-pink-50 to-indigo-100'>

            < Container maxWidth="lg" >
                <div style={{ position: 'relative' }}>

                    <MenuBar />
                    {calendar && <ScheduleXCalendar calendarApp={calendar} />}
                </div>
            </Container >

        </div>
    );
};

export default Calendar;





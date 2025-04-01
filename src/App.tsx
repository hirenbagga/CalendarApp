import './App.css'
import { ScheduleXCalendar, useCalendarApp } from "@schedule-x/react";
import { createViewWeek, createViewMonthGrid } from '@schedule-x/calendar';
import '@schedule-x/theme-default/dist/calendar.css';
import { createEventModalPlugin } from '@schedule-x/event-modal';
import { createDragAndDropPlugin } from '@schedule-x/drag-and-drop';
import { createEventsServicePlugin } from '@schedule-x/events-service';
import { useEffect, useState, useRef } from 'react';

import * as React from 'react';
import Container from '@mui/material/Container';
import BottomNavigation from '@mui/material/BottomNavigation';
import BottomNavigationAction from '@mui/material/BottomNavigationAction';
import Button from '@mui/material/Button';
import RestoreIcon from '@mui/icons-material/Restore';
import FavoriteIcon from '@mui/icons-material/Favorite';
import LocationOnIcon from '@mui/icons-material/LocationOn';

function App() {
  const [value, setValue] = useState(0);
  const eventsService = useState(() => createEventsServicePlugin())[0];

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
  }, []);

  return (
    <>
      <Container maxWidth="lg">
        {/* Wrap the button and calendar together */}
        <div style={{ position: 'relative' }}>
          {/* Button placed inside calendar */}
          <Button
            variant="contained"
            color="primary"
            onClick={() => calendar.sidebar.openAddEvent()}
            style={{
              backgroundColor: "#F1F3F4",  // Matches existing UI color
              color: "#000",               // Text color to match UI
              fontWeight: "bold",
              boxShadow: "none",
              marginRight: "10px"          // Space between "Week" and "Add Event"
            }}
          >
            ADD
          </Button>


          {/* Calendar */}
          <ScheduleXCalendar calendarApp={calendar} />
        </div>
      </Container>

      <BottomNavigation
        showLabels
        value={value}
        onChange={(event, newValue) => setValue(newValue)}
        sx={{ position: 'fixed', bottom: 0, width: '100%' }}
      >
        <BottomNavigationAction label="Recents" icon={<RestoreIcon />} />
        <BottomNavigationAction label="Favorites" icon={<FavoriteIcon />} />
        <BottomNavigationAction label="Nearby" icon={<LocationOnIcon />} />
      </BottomNavigation>
    </>
  );
}

export default App;
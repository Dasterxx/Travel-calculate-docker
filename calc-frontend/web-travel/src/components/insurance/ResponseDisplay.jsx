import React, { useState } from 'react';
import {
  Box,
  Typography,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Paper,
  Tooltip,
  IconButton,
} from '@mui/material';
import { ExpandMore, ExpandLess } from '@mui/icons-material';

function RecursiveTableRows({ data, level = 0 }) {
  const [collapsedKeys, setCollapsedKeys] = useState({});

  const toggleCollapse = (key) => {
    setCollapsedKeys(prev => ({
      ...prev,
      [key]: !prev[key]
    }));
  };

  if (data === null || data === undefined) {
    return (
      <TableRow>
        <TableCell sx={{ pl: 2 * level, fontStyle: 'italic', color: 'text.disabled' }}>null</TableCell>
        <TableCell />
      </TableRow>
    );
  }

  // Примитивы
  if (typeof data !== 'object') {
    const displayText = String(data);
    const showTooltip = displayText.length > 50;

    return (
      <TableRow>
        <TableCell sx={{ pl: 2 * level }}>
          {showTooltip ? (
            <Tooltip title={displayText}>
              <Typography noWrap sx={{ maxWidth: 300 }}>{displayText}</Typography>
            </Tooltip>
          ) : (
            displayText
          )}
        </TableCell>
        <TableCell />
      </TableRow>
    );
  }

  // Массив
  if (Array.isArray(data)) {
    if (data.length === 0) {
      return (
        <TableRow>
          <TableCell sx={{ pl: 2 * level, fontStyle: 'italic', color: 'text.disabled' }}>(empty array)</TableCell>
          <TableCell />
        </TableRow>
      );
    }
    return (
      <>
        {data.map((item, idx) => (
          <React.Fragment key={idx}>
            <TableRow>
              <TableCell sx={{ pl: 2 * level, fontWeight: 'bold', cursor: 'pointer' }} onClick={() => toggleCollapse(idx)}>
                {collapsedKeys[idx] ? <ExpandMore fontSize="small" /> : <ExpandLess fontSize="small" />} [{idx}]
              </TableCell>
            </TableRow>
            {!collapsedKeys[idx] && <RecursiveTableRows data={item} level={level + 1} />}
          </React.Fragment>
        ))}
      </>
    );
  }

  // Объект
  return (
    <>
      {Object.entries(data).map(([key, value]) => {
        const isCollapsed = collapsedKeys[key] || false;
        const isObjectOrArray = value && typeof value === 'object';

        return (
          <React.Fragment key={key}>
            <TableRow>
              <TableCell
                sx={{
                  pl: 2 * level,
                  fontWeight: 'bold',
                  cursor: isObjectOrArray ? 'pointer' : 'default',
                  userSelect: 'none',
                  display: 'flex',
                  alignItems: 'center',
                  gap: 0.5,
                }}
                onClick={() => isObjectOrArray && toggleCollapse(key)}
              >
                {isObjectOrArray ? (isCollapsed ? <ExpandMore fontSize="small" /> : <ExpandLess fontSize="small" />) : null}
                {key}
              </TableCell>
              {!isObjectOrArray ? (
                <TableCell>{String(value)}</TableCell>
              ) : (
                <TableCell sx={{ p: 0 }}>
                  {!isCollapsed && (
                    <TableContainer component={Paper} variant="outlined" sx={{ boxShadow: 'none' }}>
                      <Table size="small" aria-label="nested table">
                        <TableBody>
                          <RecursiveTableRows data={value} level={level + 1} />
                        </TableBody>
                      </Table>
                    </TableContainer>
                  )}
                </TableCell>
              )}
            </TableRow>
          </React.Fragment>
        );
      })}
    </>
  );
}

export default function ResponseDisplay({ data }) {
  if (!data) return null;

  if (data.errors) {
    return (
      <Box mt={4}>
        <Alert severity="error">
          {data.errors.map((err, idx) => (
            <div key={idx}>{err.description || err.errorCode || err.message || 'Error'}</div>
          ))}
        </Alert>
      </Box>
    );
  }

  return (
    <Box mt={4} p={3} boxShadow={2} borderRadius={2} bgcolor="background.paper">
      <Typography variant="h5" gutterBottom>
        Response Data
      </Typography>
      <TableContainer component={Paper} variant="outlined">
        <Table aria-label="response data table" size="small">
          <TableBody>
            <RecursiveTableRows data={data} />
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}

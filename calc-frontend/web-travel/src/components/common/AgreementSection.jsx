import React, { useState, useEffect } from 'react';
import {
  Box, Typography, CircularProgress, Alert, TextField, Button, Stack,
} from '@mui/material';
import AgreementList from './AgreementList';
import { useAuth } from '../../context/AuthContext';

export default function AgreementsSection() {
  const { user } = useAuth();
  const [agreements, setAgreements] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [searchUuid, setSearchUuid] = useState('');

  const isAdmin = user?.roles?.includes('ROLE_ADMIN');

  // Загрузка договоров: все или только пользователя
  const fetchAgreements = async (uuid = '') => {
    setLoading(true);
    setError('');
    try {
      let url = '/api/agreement/all';

      // Для поиска по UUID только для админа
      if (uuid && isAdmin) {
        url = `/api/agreement/find?uuid=${encodeURIComponent(uuid)}`;
      }

      const resp = await fetch(url, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      const data = await resp.json();
      if (resp.ok) {
        // Сервер вернёт либо массив, либо один объект для поиска
        let agreementsData = data.data;
        if (agreementsData && !Array.isArray(agreementsData)) {
          agreementsData = [agreementsData];
        }
        setAgreements(agreementsData || []);
      } else {
        setError(data.message || 'Failed to load agreements');
      }
    } catch {
      setError('Network error loading agreements');
    } finally {
      setLoading(false);
    }
  };

  // Первичная загрузка (и сброс поиска)
  useEffect(() => {
    if (user) {
      fetchAgreements();
    }
    // eslint-disable-next-line
  }, [user]);

  // UI поиска по UUID (только для администратора)
  return (
    <Box>
      {isAdmin && (
        <Stack direction="row" spacing={2} mb={2}>
          <TextField
            label="Find by UUID"
            size="small"
            value={searchUuid}
            onChange={e => setSearchUuid(e.target.value)}
            autoComplete="off"
          />
          <Button
            variant="contained"
            onClick={() => fetchAgreements(searchUuid)}
            disabled={!searchUuid}
          >
            Search
          </Button>
          <Button
            onClick={() => {
              setSearchUuid('');
              fetchAgreements();
            }}
            variant="outlined"
            disabled={loading}
          >
            Show All
          </Button>
        </Stack>
      )}
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {loading
        ? (
            <Box textAlign="center" mt={2}><CircularProgress /></Box>
          )
        : (
            <AgreementList agreements={agreements} />
          )}
    </Box>
  );
}

import React, { useState, useEffect } from 'react';
import { Box, Typography, CircularProgress, Alert, Tabs, Tab, Paper } from '@mui/material';
import AgreementTable from '../components/admin/AgreementTable';
import AgreementEditDialog from '../components/admin/AgreementEditDialog';

export default function AdminAgreementsPanel() {
  const [agreements, setAgreements] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [tabIndex, setTabIndex] = useState(0);
  const [editAgreement, setEditAgreement] = useState(null);

  useEffect(() => {
    fetchAgreements();
  }, []);

  const fetchAgreements = async () => {
    setLoading(true);
    setError('');
    try {
      const resp = await fetch('/api/agreement/all', {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      const data = await resp.json();
      if (resp.ok) {
        setAgreements(data.data || []);
      } else {
        setError(data.message || 'Failed to load agreements');
      }
    } catch {
      setError('Network error loading agreements');
    } finally {
      setLoading(false);
    }
  };

  const handleAgreementUpdated = updated => {
    setAgreements(prev => prev.map(a => (a.uuid === updated.uuid ? updated : a)));
    setEditAgreement(null);
  };

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto', mt: 4, p: 3, bgcolor: 'background.paper', borderRadius: 2, boxShadow: 3 }}>
      <Typography variant="h4" mb={3}>Admin Agreements</Typography>

      {error && <Alert severity="error" mb={2}>{error}</Alert>}
      {loading ? (
        <Box textAlign="center"><CircularProgress /><Typography mt={2}>Loading agreements...</Typography></Box>
      ) : (
        <Paper>
          <AgreementTable
            agreements={agreements}
            onEdit={setEditAgreement}
            onReload={fetchAgreements}
            // onDelete реализован внутри таблицы
          />
        </Paper>
      )}

      {editAgreement && (
        <AgreementEditDialog
          open={Boolean(editAgreement)}
          agreement={editAgreement}
          onClose={() => setEditAgreement(null)}
          onSave={handleAgreementUpdated}
        />
      )}
    </Box>
  );
}

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  Box,
  Card,
  CardContent,
  Button,
  Typography,
  Alert,
  CircularProgress,
  useTheme
} from '@mui/material';

import CountriesSection from './CountriesSection';
import DatesSection from './DatesSection';
import PersonsSection from './PersonsSection';
import InsuranceSection from './InsuranceSection';
import ResponseDisplay from './ResponseDisplay';

export default function TravelInsuranceForm({ onCalculate }) {
  const [countries, setCountries] = useState(['']);
  const [agreementDateFrom, setAgreementDateFrom] = useState('');
  const [agreementDateTo, setAgreementDateTo] = useState('');
  const [persons, setPersons] = useState([{
    firstName: '',
    lastName: '',
    birthDate: '',
    personCode: '',
    roles: []
  }]);
  const [insuranceLimit, setInsuranceLimit] = useState('10000');
  const [selectedRisks, setSelectedRisks] = useState(['TRAVEL_MEDICAL']);
  const [responseData, setResponseData] = useState(null);
  const [errors, setErrors] = useState([]);
  const [loading, setLoading] = useState(false);
  const { user } = useAuth();
  const navigate = useNavigate();

  const theme = useTheme();

  const addCountry = () => setCountries([...countries, '']);
  const handleCountryChange = (idx, val) => {
    const arr = [...countries];
    arr[idx] = val;
    setCountries(arr);
  };
  const removeCountry = (idx) => {
    if (countries.length <= 1) return;
    const arr = [...countries];
    arr.splice(idx, 1);
    setCountries(arr);
  };

  const handlePersonChange = (idx, field, val) => {
    const arr = [...persons];
    arr[idx] = { ...arr[idx], [field]: val };
    setPersons(arr);
  };


  useEffect(() => {
      if (
        user &&
        persons.length === 1 &&
        !persons[0].firstName &&
        !persons[0].lastName &&
        !persons[0].birthDate
      ) {
        setPersons([{
          firstName: user.firstName || '',
          lastName: user.lastName || '',
          birthDate: user.birthDate || '',
          personCode: user.personCode || '',
          roles: user.roles || []
        }]);
      }
    }, [user]);
  const addPerson = () => setPersons([...persons, {
      firstName: '',
      lastName: '',
      birthDate: '',
    }]);
  const removePerson = (idx) => {
    if (persons.length <= 1) return;
    const arr = [...persons];
    arr.splice(idx, 1);
    setPersons(arr);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!user) {
      navigate('/auth?type=login&redirect=/insurance');
      return;
    }

    setLoading(true);
    setErrors([]);
    setResponseData(null);

    try {
      const token = localStorage.getItem('token'); // Получаем токен из localStorage

      const formatDateTimeForBackend = (dateTimeString) => {
      if (!dateTimeString) return null; // Или возвращайте пустую строку, если бэкенд может обработать null
      if (dateTimeString.length === 16) { // YYYY-MM-DDTHH:mm
        return `${dateTimeString}:00`; // Добавляем секунды
      }
      return dateTimeString; // Если уже есть секунды или другой формат
    };
      const requestPayload = {
        agreement: {
          countriesToVisit: countries.filter(Boolean),
          agreementDateFrom: formatDateTimeForBackend(agreementDateFrom),
          agreementDateTo: formatDateTimeForBackend(agreementDateTo),
          persons: persons.map(p => ({
            firstName: p.firstName,
            lastName: p.lastName,
            birthDate: p.birthDate,
            personCode: p.personCode,
            roles: p.roles
          })),
          insuranceLimit: parseInt(insuranceLimit, 10),
          selectedRisks
        }
      };

      const response = await fetch('/insurance/travel/web/v3', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}` // Добавляем токен в заголовок
        },
        body: JSON.stringify(requestPayload),
      });

      const data = await response.json();

      if (!response.ok) {
        // Проверяем ошибку в data.errors, потом в data.data.errors, если структура вложенная
        let backendErrors = [];

        if (data.errors && Array.isArray(data.errors)) {
          backendErrors = data.errors;
        } else if (data.data && Array.isArray(data.data.errors)) {
          backendErrors = data.data.errors;
        }

        if (backendErrors.length > 0) {
          const errorMessages = backendErrors.map(
            err => err.description || err.errorCode || 'Unknown error'
          );
          setErrors(errorMessages);
        } else {
          setErrors([data.message || 'An error occurred']);
        }
      } else {
        setResponseData(data);
        if (onCalculate) onCalculate(data);
      }
    } catch (err) {
      setErrors([err.message]);
    } finally {
      setLoading(false);
    }
  };


  return (
      <Box
        maxWidth={700}
        mx="auto"
        p={{ xs: 0, sm: 3 }}
        mt={4}
        // Добавляем небольшое затемнение для текста на фоне!
        sx={{
          // Смягчаем фон, делаем чуть прозрачнее или затемняем
          backgroundColor: 'rgba(255,255,255,0.90)',
          borderRadius: 3,
          boxShadow: 6,
          // чтобы контур не был слишком бледным, добавим легкий outline
          border: `1px solid ${theme.palette.divider}`,
        }}
      >
        <CardContent
          sx={{
            color: '#222', // Тёмный текст
          }}
        >
          <Typography
            variant="h4"
            mb={3}
            textAlign="center"
            sx={{
              color: theme.palette.text.primary,
              textShadow: '0 2px 6px rgba(0,0,0,0.13)', // чтобы на любом фоне было читабельно
            }}
          >
            Travel Insurance Form
          </Typography>

          <form onSubmit={handleSubmit} noValidate>
            <CountriesSection
              countries={countries}
              onAdd={addCountry}
              onChange={handleCountryChange}
              onRemove={removeCountry}
            />

            <DatesSection
              from={agreementDateFrom}
              to={agreementDateTo}
              onFromChange={e => setAgreementDateFrom(e.target.value)}
              onToChange={e => setAgreementDateTo(e.target.value)}
            />

            <PersonsSection
              persons={persons}
              onChange={handlePersonChange}
              onAddPerson={addPerson}
              onRemovePerson={removePerson}
            />

            <InsuranceSection
              insuranceLimit={insuranceLimit}
              onLimitChange={e => setInsuranceLimit(e.target.value)}
              selectedRisks={selectedRisks}
              onRisksChange={e => setSelectedRisks([e.target.value])}
            />

            <Box display="flex" gap={2} justifyContent="center" mt={3} flexWrap="wrap">
              <Button type="submit" variant="contained" color="primary" disabled={loading || !user}>
                {loading ? <CircularProgress size={24} color="inherit" /> : 'Calculate Premium'}
              </Button>
              <Button
                type="button"
                variant="outlined"
                color="secondary"
                onClick={() => window.location.reload()}
              >
                Clear
              </Button>
            </Box>

            {!user && (
              <Alert severity="warning" sx={{ mt: 3 }}>
                You must be logged in to calculate insurance.{' '}
                <Button color="inherit" size="small" onClick={() => navigate('/auth?type=login')}>
                  Login Now
                </Button>
              </Alert>
            )}

            {errors.length > 0 && (
              <Alert severity="error" sx={{ mt: 3 }}>
                <Typography variant="h6">Errors:</Typography>
                {errors.map((err, idx) => <div key={idx}>{err}</div>)}
              </Alert>
            )}
          </form>
          {responseData && <ResponseDisplay data={responseData} />}
        </CardContent>
      </Box>
    );
}

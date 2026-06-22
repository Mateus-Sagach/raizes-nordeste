-- A V9 tentou corrigir as senhas do seed, mas o hash usado nela tambem estava errado
-- Este hash foi gerado e verificado com a biblioteca bcrypt antes de ser usado aqui, confirmando que bate com a senha "password".
UPDATE usuarios SET senha_hash = '$2a$10$4dI5fiFp2HW0P/6l6aUvd.XxtsU7feAdOlRVbPwcn4.q6cHjesCIS'
WHERE email IN (
    'admin@raizes.com',
    'cliente@teste.com',
    'cozinha@raizes.com',
    'gerente@raizes.com',
    'balcao@raizes.com'
);
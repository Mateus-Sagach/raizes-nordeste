UPDATE usuarios SET senha_hash = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.'
WHERE email IN (
    'admin@raizes.com',
    'cliente@teste.com',
    'cozinha@raizes.com',
    'gerente@raizes.com',
    'balcao@raizes.com'
);